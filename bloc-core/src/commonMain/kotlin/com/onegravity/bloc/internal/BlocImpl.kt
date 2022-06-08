package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.BlocExtension
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

internal class BlocImpl<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    initialize: Initializer<State, Action>? = null,
    thunks: List<MatcherThunk<State, Action, Action>> = emptyList(),
    reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>,
    initDispatcher: CoroutineContext = Dispatchers.Default,
    thunkDispatcher: CoroutineContext = Dispatchers.Default,
    reduceDispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, SideEffect>(),
    BlocExtension<State, Action, SideEffect, Proposal> {

    // ReduceProcessor, ThunkProcessor and InitializeProcessor need to be defined in that exact
    // order or we risk NullPointerExceptions
    private val reduceProcessor = ReduceProcessor(
        blocContext,
        blocState,
        reducers,
        reduceDispatcher
    )

    private val thunkProcessor = ThunkProcessor(
        blocContext,
        blocState,
        thunks,
        thunkDispatcher,
        reduceProcessor::runReducers
    )

    private val initializeProcessor = InitializeProcessor(
        blocContext,
        blocState,
        initialize,
        initDispatcher
    ) { send(it) }

    override val value
        get() = blocState.value

    override val sideEffects: SideEffectStream<SideEffect> = reduceProcessor.sideEffects

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun send(action: Action) {
        logger.d("emit action ${action.trimOutput()}")
        if (! thunkProcessor.send(action)) {
            reduceProcessor.send(action)
        }
    }

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
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        observerLifecycle.doOnStart {
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
        }

        observerLifecycle.doOnStop {
            logger.d("stop Bloc subscription")
            coroutineScope.cancel("stop Bloc subscription")
        }
    }

    /**
     * Public API (interface BlocExtension) to run thunks / reducers etc. MVVM+ style
     */

    override fun initialize(initialize: Initializer<State, Action>) {
        initializeProcessor.initialize(initialize)
    }

    override fun reduce(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        reduceProcessor.reduce(reduce)
    }

    override fun thunk(thunk: ThunkNoAction<State, Action>) {
        thunkProcessor.thunk(thunk)
    }

}
