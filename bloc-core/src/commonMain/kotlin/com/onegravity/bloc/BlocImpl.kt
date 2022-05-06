package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext

internal class BlocImpl<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val initializer: Initializer<State, Action> = { },
    private val thunks: List<MatcherThunk<State, Action, Action>> = emptyList(),
    private val reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, SideEffect>() {

    private val thunkChannel = Channel<Action>(UNLIMITED)

    private val reduceChannel = Channel<Action>(UNLIMITED)

    private val sideEffectChannel = Channel<SideEffect>(UNLIMITED)

    // we use this scope internally, it's tied to the lifecycle of the BlocInstance and will be
    // cancelled when the InstanceKeeper destroys the Bloc (onDestroy())
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val lifecycle = BlocLifecycle(
        { coroutineScope.start() },
        { coroutineScope.cancel() }
    )

    override val value get() = blocState.value

    override val sideEffects: SideEffectStream<SideEffect> = sideEffectChannel.receiveAsFlow()

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun send(action: Action) {
        logger.d("emit action $action")
        when (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            true -> thunkChannel.trySend(action)
            else -> reduceChannel.trySend(action)
        }
    }

    override suspend fun collect(collector: FlowCollector<State>) {
        blocState.collect(collector)
    }

    /**
     * Use this in iOS/Swift only
     */
    override fun observe(
        lifecycle: Lifecycle, state:
        BlocObserver<State>?, sideEffect:
        BlocObserver<SideEffect>?
    ) {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        lifecycle.doOnStart {
            logger.d("onStart -> start subscription")
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
        }

        lifecycle.doOnStop {
            logger.d("onStop -> stop subscription")
            coroutineScope.cancel("Stop Subscription")
        }
    }

    /**
     * This needs to come after all variable declarations to make sure everything is initialized
     * before the Bloc is started
     */
    init {
        with(blocContext.lifecycle) {
            doOnStart { lifecycle.transition(LifecycleEvent.Start) }
            doOnStop { lifecycle.transition(LifecycleEvent.Stop) }
        }
    }

    private fun CoroutineScope.start() {
        launch(dispatcher) {
            val context = InitializerContext<State, Action>(value) { action -> send(action) }
            context.initializer()
        }.invokeOnCompletion {
            launch(dispatcher) {
                for (action in thunkChannel) {
                    runThunks(action)
                }
            }
            launch(dispatcher) {
                for (action in reduceChannel) {
                    runReducers(action)
                }
            }
        }
    }

    private suspend fun runThunks(action: Action) {
        logger.d("process action $action")
        thunks.forEachIndexed { index, (matcher, _) ->
            if (matcher == null || matcher.matches(action)) {
                runThunk(index, action)
            }
        }
    }

    private suspend fun runThunk(index: Int, action: Action) {
        val dispatcher: Dispatcher<Action> = {
            nextThunkDispatcher(index + 1, it).invoke(it)
        }
        val thunk = thunks[index].thunk
        ThunkContext({ blocState.value }, action, dispatcher).thunk()
    }

    private fun nextThunkDispatcher(startIndex: Int, action: Action): Dispatcher<Action> {
        (startIndex..thunks.lastIndex).forEach { index ->
            val matcher = thunks[index].matcher
            if (matcher == null || matcher.matches(action)) {
                return { runThunk(index, action) }
            }
        }

        return { runReducers(action) }
    }

    private suspend fun runReducers(action: Action) {
        getMatchingReducers(action).fold(false) { proposalEmitted, matcherReducer ->
            val (_, reducer, expectsProposal) = matcherReducer
            when {
                !expectsProposal -> {                  // running sideEffect { }
                    reducer.runReducer(action)
                    proposalEmitted
                }
                !proposalEmitted -> {                  // running reduce { } or reduceAnd { }
                    reducer.runReducer(action)
                    true
                }
                else -> proposalEmitted                 // skipping reduce { } or reduceAnd { }
            }
        }
    }

    private suspend fun Reducer<State, Action, Effect<Proposal, SideEffect>>.runReducer(
        action: Action
    ): Boolean {
        val (proposal, sideEffects) = ReducerContext(blocState.value, action, coroutineScope).this()
        return if (proposal != null) {
            blocState.send(proposal)
            postSideEffects(sideEffects)
            true
        } else {
            postSideEffects(sideEffects)
            false
        }
    }

    private fun getMatchingReducers(action: Action) = reducers
        .filter { it.matcher == null || it.matcher.matches(action) }
        .also {
            if (it.isEmpty()) logger.e("No reducer found, did you define reduce { }?")
        }

    private suspend fun postSideEffects(sideEffects: List<SideEffect>) {
        sideEffects.forEach {
            sideEffectChannel.send(it)
        }
    }

    /**
     * Public API to run thunks / reducers "externally" (using extension functions)
     */

    /**
     * The reducer runs synchronously!
     */
    fun runReducer(reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>) = coroutineScope.launch {
        val (proposal, sideEffects) = ReducerContextNoAction(
            blocState.value,
            coroutineScope
        ).reducer()
        if (proposal != null) {
            blocState.send(proposal)
            postSideEffects(sideEffects)
        } else {
            postSideEffects(sideEffects)
        }
    }

    fun runThunk(
        coroutineScope: CoroutineScope?,
        thunk: ThunkNoAction<State, Action>
    ) {
        val scope = (coroutineScope ?: this.coroutineScope) + dispatcher
        scope.launch {
            val dispatcher: Dispatcher<Action> = {
                nextThunkDispatcher(0, it).invoke(it)
            }
            ThunkContextNoAction({ blocState.value }, dispatcher).thunk()
        }
    }

    fun runInitializer(initializer: Initializer<State, Action>) = coroutineScope.launch {
        val context = InitializerContext<State, Action>(value) { action -> send(action) }
        context.initializer()
    }

}
