package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.internal.builder.MatcherReducer
import com.onegravity.bloc.internal.builder.MatcherThunk
import com.onegravity.bloc.internal.lifecycle.BlocLifecycle
import com.onegravity.bloc.internal.lifecycle.BlocLifecycleImpl
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector

/**
 * The probably most important class in the framework.
 *
 * Implements Bloc and BlocExtension and is responsible for executing initializers, reducers and
 * thunks.
 */
internal class BlocImpl<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    initialize: Initializer<State, Action>? = null,
    thunks: List<MatcherThunk<State, Action, Action>> = emptyList(),
    reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>,
    initDispatcher: CoroutineDispatcher = Dispatchers.Default,
    thunkDispatcher: CoroutineDispatcher = Dispatchers.Default,
    reduceDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Bloc<State, Action, SideEffect>(),
    BlocExtension<State, Action, SideEffect, Proposal> {

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    private val blocLifecycle: BlocLifecycle = BlocLifecycleImpl(blocContext.lifecycle)

    /* ******************************************************************************************
     * ReduceProcessor, ThunkProcessor and InitializeProcessor need to be defined in this exact *
     * order or we risk NullPointerExceptions                                                   *
     ********************************************************************************************/

    private val reduceProcessor = ReduceProcessor(
        blocLifecycle,
        blocState,
        reducers,
        reduceDispatcher
    )

    private val thunkProcessor = ThunkProcessor(
        blocLifecycle,
        blocState,
        thunks,
        thunkDispatcher,
        reduceProcessor::send
    )

    private val initializeProcessor = InitializeProcessor(
        blocLifecycle,
        blocState,
        initialize,
        initDispatcher
    ) { send(it) }

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
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun send(action: Action) {
        logger.d("emit action ${action.trimOutput()}")
        // thunks are always processed first
        // ThunkProcessor will send the action to ReduceProcessor if there's no matching thunk
        thunkProcessor.send(action)
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
    override fun initialize(initialize: Initializer<State, Action>) {
        initializeProcessor.initialize(initialize)
    }

    /**
     * BlocExtension interface implementation:
     * reduce { } -> run a Reducer MVVM+ style
     */
    override fun reduce(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        reduceProcessor.reduce(reduce)
    }

    /**
     * BlocExtension interface implementation:
     * thunk { } -> run a thunk MVVM+ style
     */
    override fun thunk(thunk: ThunkNoAction<State, Action>) {
        thunkProcessor.thunk(thunk)
    }

}
