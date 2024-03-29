package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.internal.builder.MatcherReducer
import com.onegravity.bloc.internal.builder.MatcherThunk
import com.onegravity.bloc.internal.lifecycle.BlocLifecycle
import com.onegravity.bloc.internal.lifecycle.BlocLifecycleImpl
import com.onegravity.bloc.internal.lifecycle.subscribe
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.BlocObserver
import com.onegravity.bloc.utils.Effect
import com.onegravity.bloc.utils.Initializer
import com.onegravity.bloc.utils.ReducerNoAction
import com.onegravity.bloc.utils.SideEffectStream
import com.onegravity.bloc.utils.ThunkNoAction
import com.onegravity.bloc.utils.logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.suspendCoroutine

private const val QUEUE_INITIAL_SIZE = 10

/**
 * The probably most important class in the framework.
 *
 * Implements Bloc and BlocExtension and is responsible for executing initializers, reducers and
 * thunks.
 */
@Suppress("LongParameterList")
internal class BlocImpl<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    initialize: Initializer<State, Action, Proposal>? = null,
    thunks: List<MatcherThunk<State, Action, Action, Proposal>> = emptyList(),
    reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>,
    initDispatcher: CoroutineDispatcher = Dispatchers.Default,
    thunkDispatcher: CoroutineDispatcher = Dispatchers.Default,
    reduceDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Bloc<State, Action, SideEffect>(),
    BlocExtension<State, Action, SideEffect, Proposal> {

    private val blocLifecycle: BlocLifecycle = BlocLifecycleImpl(blocContext.lifecycle)

    /* ******************************************************************************************
     * ReduceProcessor, ThunkProcessor and InitializeProcessor need to be defined in this exact *
     * order or we risk NullPointerExceptions                                                   *
     ********************************************************************************************/

    private inner class ActionQueueElement(
        val action: Action? = null,
        val thunk: ThunkNoAction<State, Action, Proposal>? = null,
        val reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>? = null
    )

    /**
     * Queue for thunks and reducers submitted while the bloc is being initialized (initializer is
     * running). These thunks/reducers are processed once the bloc transitions to the Started state.
     */
    private val actionQueue = ArrayDeque<ActionQueueElement>(QUEUE_INITIAL_SIZE)

    private val reduceProcessor = ReduceProcessor(
        lifecycle = blocLifecycle,
        state = blocState,
        dispatcher = reduceDispatcher,
        reducers = reducers
    )

    /**
     *  Function that sends the proposal directly to the BlocState and waits for the process to
     *  finish by suspending execution.
     */
    private val reducer: suspend (proposal: Proposal) -> Unit = { proposal ->
        suspendCoroutine { continuation ->
            reduceProcessor.reduce({ Effect(proposal, emptyList()) }, continuation)
        }
    }

    /**
     * Reduces the given action by suspending the coroutine until the action has been processed.
     */
    private suspend fun reduce(action: Action) = suspendCoroutine { continuation ->
        reduceProcessor.send(action, continuation)
    }

    private val thunkProcessor = ThunkProcessor(
        lifecycle = blocLifecycle,
        state = blocState,
        dispatcher = thunkDispatcher,
        thunks = thunks,
        dispatch = { reduce(it) },
        reduce = reducer
    )

    private val initializeProcessor = InitializeProcessor(
        lifecycle = blocLifecycle,
        state = blocState,
        dispatcher = initDispatcher,
        initializer = initialize,
        dispatch = {
            val processed = thunkProcessor.send(it)
            if (processed.not()) reduce(it)
        },
        reduce = reducer
    )

    init {
        blocLifecycle.subscribe(onStart = {
            // process actions, thunks and reducers
            while (!actionQueue.isEmpty()) {
                val entry = actionQueue.removeFirst()
                entry.action?.run(::send)
                entry.thunk?.run(thunkProcessor::thunk)
                entry.reducer?.run(reduceProcessor::reduce)
            }
        })
    }

    /**
     * The current state
     */
    override val value
        get() = blocState.value

    /**
     * Flow for side effects (outgoing)
     */
    override val sideEffects: SideEffectStream<SideEffect> = reduceProcessor.sideEffects

    /**
     * The Sink to dispatch Actions.
     * All it does is add the Action to a queue to be processed asynchronously.
     */
    override fun send(action: Action) {
        when {
            // we need to cache actions if the initializer is still running
            blocLifecycle.isStarting() -> actionQueue += ActionQueueElement(action = action)

            // thunks are always processed first
            // ThunkProcessor will send the action to ReduceProcessor if there's no matching thunk
            blocLifecycle.isStarted() -> {
                val processed = thunkProcessor.send(action)
                if (processed.not()) {
                    // reducers are meant to run on the main thread -> using runBlocking here is OK
                    runBlocking {
                        reduce(action)
                    }
                }
            }

            else -> { /* NOP*/ }
        }
    }

    /**
     * StateStream.collect(FlowCollector<Value>)
     */
    override suspend fun collect(collector: FlowCollector<State>) {
        blocState.collect(collector)
    }

    /**
     * Use this in iOS/Swift.
     * It's the equivalent of the subscribe extension function (for Android)
     */
    override fun observe(
        observerLifecycle: Lifecycle, state:
        BlocObserver<State>?, sideEffect:
        BlocObserver<SideEffect>?
    ) {
        observerLifecycle.doOnStart {
            val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

            logger.d("start Bloc subscription")
            state?.let {
                coroutineScope.launch {
                    collect { state(it) }
                }
            }
            sideEffect?.let {
                coroutineScope.launch {
                    sideEffects.collect { sideEffect(it) }
                }
            }

            observerLifecycle.doOnStop {
                logger.d("stop Bloc subscription")
                coroutineScope.cancel("stop Bloc subscription")
            }
        }
    }

    /**
     * BlocExtension interface implementation:
     * onCreate { } -> run an initializer MVVM+ style
     */
    override fun initialize(initialize: Initializer<State, Action, Proposal>) {
        initializeProcessor.initialize(initialize)
    }

    /**
     * BlocExtension interface implementation:
     * reduce { } -> run a Reducer MVVM+ style
     */
    override fun reduce(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        when {
            // we need to cache if the initializer is still running
            blocLifecycle.isStarting() -> actionQueue += ActionQueueElement(reducer = reduce)
            blocLifecycle.isStarted() -> reduceProcessor.reduce(reduce)
            else -> { /* NOP*/ }
        }
    }

    /**
     * BlocExtension interface implementation:
     * thunk { } -> run a thunk MVVM+ style
     */
    override fun thunk(thunk: ThunkNoAction<State, Action, Proposal>) {
        when {
            // we need to cache if the initializer is still running
            blocLifecycle.isStarting() -> actionQueue += ActionQueueElement(thunk = thunk)
            blocLifecycle.isStarted() -> thunkProcessor.thunk(thunk)
            else -> { /* NOP*/ }
        }
    }

}
