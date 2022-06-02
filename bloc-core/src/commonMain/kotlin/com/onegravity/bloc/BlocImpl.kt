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
    private val initialize: Initializer<State, Action> = { },
    private val thunks: List<MatcherThunk<State, Action, Action>> = emptyList(),
    private val reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>,
    // todo have separate Dispatchers for initialize, reducers, side effects and thunks
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, SideEffect>(),
    BlocExtension<State, Action, SideEffect, Proposal> {

    private val thunkChannel = Channel<Action>(UNLIMITED)

    private val reduceChannel = Channel<Action>(UNLIMITED)

    private val sideEffectChannel = Channel<SideEffect>(UNLIMITED)

    // we use this scope internally, it's tied to the lifecycle of the Bloc and will be
    // cancelled when the InstanceKeeper destroys the Bloc (onDestroy())
    private var coroutineScope: CoroutineScope? = null

    override val value
        get() = blocState.value

    override val sideEffects: SideEffectStream<SideEffect> = sideEffectChannel.receiveAsFlow()

    /**
     * This needs to come after all variable/property declarations to make sure everything is initialized
     * before the Bloc is started
     */
    init {
        blocContext.lifecycle.toBlocLifecycle(
            onCreate = {
                coroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
                coroutineScope?.initialize()
            },
            onStart = { coroutineScope?.start() },
            onStop = { coroutineScope?.cancel() },
            onDestroy = { /* nothing to do */ }
        )
    }

    // actions can be complex objects resulting in lots of debug output
    private fun Action.trim() = toString().take(100)

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun send(action: Action) {
        logger.d("emit action ${action.trim()}")
        when (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            true -> thunkChannel.trySend(action)
            else -> reduceChannel.trySend(action)
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

    private fun CoroutineScope.initialize() {
        launch(dispatcher) {
            val context = InitializerContext(
                state = value,
                coroutineScope = this,
                dispatch = { action: Action -> send(action) }
            )
            context.initialize()
        }
    }

    private fun CoroutineScope.start() {
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

    private suspend fun runThunks(action: Action) {
        logger.d("run thunks for action ${action.trim()}")
        thunks.forEachIndexed { index, (matcher, _) ->
            if (matcher == null || matcher.matches(action)) {
                runThunk(index, action)
            }
        }
    }

    private suspend fun runThunk(index: Int, action: Action) {
        coroutineScope?.run {
            val dispatcher: Dispatcher<Action> = {
                nextThunkDispatcher(index + 1, it).invoke(it)
            }
            val thunk = thunks[index].thunk
            ThunkContext({ blocState.value }, action, dispatcher, this).thunk()
        }
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
        logger.d("run reducers for action ${action.trim()}")
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
    ): Boolean = coroutineScope?.run {
        val reduce = this@runReducer
        val context = ReducerContext(blocState.value, action, this)
        val (proposal, sideEffects) = context.reduce()
        return if (proposal != null) {
            blocState.send(proposal)
            postSideEffects(sideEffects)
            true
        } else {
            postSideEffects(sideEffects)
            false
        }
    } ?: false

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

    override fun runInitializer(initialize: Initializer<State, Action>) = coroutineScope?.launch {
        val context = InitializerContext(
            state = value,
            coroutineScope = this,
            dispatch = { action: Action -> send(action) }
        )
        context.initialize()
    }

    /**
     * The reducer runs synchronously!
     */
    override fun runReducer(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>): Job? =
        coroutineScope?.launch {
            val context = ReducerContextNoAction(blocState.value, this)
            val (proposal, sideEffects) = context.reduce()
            proposal?.let { blocState.send(it) }
            postSideEffects(sideEffects)
        }

    override fun runThunk(thunk: ThunkNoAction<State, Action>) {
        coroutineScope?.run {
            launch {
                val dispatcher: Dispatcher<Action> = {
                    nextThunkDispatcher(0, it).invoke(it)
                }
                val context = ThunkContextNoAction(
                    getState = { blocState.value },
                    dispatch = dispatcher,
                    coroutineScope = this
                )
                context.thunk()
            }
        }
    }

}
