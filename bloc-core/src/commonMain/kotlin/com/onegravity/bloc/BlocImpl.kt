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
    private val initDispatcher: CoroutineContext = Dispatchers.Default,
    private val thunkDispatcher: CoroutineContext = Dispatchers.Default,
    private val reduceDispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, SideEffect>(),
    BlocExtension<State, Action, SideEffect, Proposal> {

    private val thunkChannel = Channel<Action>(UNLIMITED)

    private val reduceChannel = Channel<Action>(UNLIMITED)

    private val sideEffectChannel = Channel<SideEffect>(UNLIMITED)

    // we use these scopes internally
    // they are tied to the lifecycle of the Bloc and will be cancelled when the Bloc is destroyed
    private var initScope: CoroutineScope? = null
    private var thunkScope: CoroutineScope? = null
    private var reduceScope: CoroutineScope? = null

    override val value
        get() = blocState.value

    override val sideEffects: SideEffectStream<SideEffect> = sideEffectChannel.receiveAsFlow()

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        blocContext.lifecycle.doOnCreate {
            logger.d("onCreate -> initialize Bloc")
            initScope = CoroutineScope(SupervisorJob() + initDispatcher)
            initScope?.initialize()
        }
        blocContext.lifecycle.doOnStart {
            logger.d("onStart -> start Bloc")
            thunkScope = CoroutineScope(SupervisorJob() + thunkDispatcher)
            reduceScope = CoroutineScope(SupervisorJob() + reduceDispatcher)
            thunkScope?.startThunks()
            reduceScope?.startReducers()
        }

        blocContext.lifecycle.doOnStop {
            logger.d("onStop -> stop Bloc")
            thunkScope?.cancel()
            reduceScope?.cancel()
        }

        blocContext.lifecycle.doOnDestroy {
            logger.d("onDestroy -> destroy Bloc")
            initScope?.cancel()
        }
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

    // triggered as initScope?.initialize() -> uses initScope
    private fun CoroutineScope.initialize() {
        launch {
            val context = InitializerContext(
                state = value,
                coroutineScope = this,
                dispatch = { action: Action -> send(action) }
            )
            context.initialize()
        }
    }

    // triggered as thunkScope?.startThunks() -> uses thunkScope
    private fun CoroutineScope.startThunks() {
        launch {
            for (action in thunkChannel) {
                runThunks(action)
            }
        }
    }

    // triggered as reduceScope?.startReducers() -> uses reduceScope
    private fun CoroutineScope.startReducers() {
        launch {
            for (action in reduceChannel) {
                runReducers(action)
            }
        }
    }

    private suspend fun runThunks(action: Action, startIndex: Int = 0) {
        logger.d("run thunks for action ${action.trim()}")
        (startIndex..thunks.lastIndex).forEach { index ->
            val (matcher, _) = thunks[index]
            if (matcher == null || matcher.matches(action)) {
                runThunk(action, index)
            }
        }
    }

    private suspend fun runThunk(action: Action, index: Int) {
        thunkScope?.run {
            val dispatcher: Dispatcher<Action> = {
                nextThunkDispatcher(it, index + 1).invoke(it)
            }
            val thunk = thunks[index].thunk
            ThunkContext({ blocState.value }, action, dispatcher, this).thunk()
        }
    }

    private fun nextThunkDispatcher(action: Action, startIndex: Int = 0): Dispatcher<Action> {
        (startIndex..thunks.lastIndex).forEach { index ->
            val matcher = thunks[index].matcher
            if (matcher == null || matcher.matches(action)) {
                return { runThunks(action, index) }
            }
        }

        return { runReducers(action) }
    }

    private suspend fun runReducers(action: Action) {
        logger.d("run reducers for action ${action.trim()}")
        getMatchingReducers(action).fold(false) { proposalEmitted, matcherReducer ->
            val (_, reducer, expectsProposal) = matcherReducer
            when {
                // running sideEffect { } -> always run no matter what
                ! expectsProposal -> {
                    reducer.runReducer(action)
                    proposalEmitted
                }

                // running reduce { } or reduceAnd { }
                // -> only run if state wasn't reduced already (proposalEmitted = true)
                ! proposalEmitted -> {
                    reducer.runReducer(action)
                    true
                }

                // reduce { } or reduceAnd { } but state was already reduced (proposalEmitted = true)
                else -> proposalEmitted
            }
        }
    }

    private suspend fun Reducer<State, Action, Effect<Proposal, SideEffect>>.runReducer(
        action: Action
    ): Boolean = reduceScope?.run {
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
     * Public API (interface BlocExtension) to run thunks / reducers etc. MVVM+ style
     */

    override fun runInitializer(initialize: Initializer<State, Action>) = initScope?.launch {
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
        reduceScope?.launch {
            val context = ReducerContextNoAction(blocState.value, this)
            val (proposal, sideEffects) = context.reduce()
            proposal?.let { blocState.send(it) }
            postSideEffects(sideEffects)
        }

    override fun runThunk(thunk: ThunkNoAction<State, Action>) {
        thunkScope?.run {
            launch {
                val dispatcher: Dispatcher<Action> = {
                    nextThunkDispatcher(it).invoke(it)
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
