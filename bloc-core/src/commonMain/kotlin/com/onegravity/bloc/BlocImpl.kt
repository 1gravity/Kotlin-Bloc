package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext

class BlocImpl<State, Action: Any, SideEffect, Proposal>(
    override val blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val initializer: Initializer<State, Action> = { },
    private val thunks: List<MatcherThunk<State, Action>> = emptyList(),
    private val reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, SideEffect, Proposal> {

    private val thunkChannel = Channel<Action>(UNLIMITED)

    private val reduceChannel = Channel<Action>(UNLIMITED)

    private val sideEffectChannel = Channel<SideEffect>(UNLIMITED)

    init {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        blocContext.lifecycle.doOnCreate {
            logger.d("onCreate -> start Bloc")
            coroutineScope.start()
        }

        blocContext.lifecycle.doOnDestroy {
            logger.d("onDestroy -> stop Bloc")
            coroutineScope.cancel("Stop Bloc")
        }
    }

    override val value
        get() = blocState.value

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun emit(action: Action) {
        logger.d("emit action $action")
        when (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            true -> thunkChannel.trySend(action)
            else -> reduceChannel.trySend(action)
        }
    }

    override suspend fun collect(collector: FlowCollector<State>) {
        blocState.collect(collector)
    }

    override val sideEffects: Stream<SideEffect> = sideEffectChannel.receiveAsFlow()

    private fun CoroutineScope.start() {
        launch(dispatcher) {
            val context = InitializerContext<State, Action>(value) { action -> emit(action) }
            context.initializer()
        }

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
        getMatchingReducers(action)
            .fold(false) { proposalEmitted, matcherReducer ->
                val (_, reducer, expectsProposal) = matcherReducer
                when {
                    ! expectsProposal -> {                  // running sideEffect { }
                        reducer.runReducer(action)
                        proposalEmitted
                    }
                    ! proposalEmitted -> {                  // running reduce { } or state { }
                        reducer.runReducer(action)
                        true
                    }
                    else -> proposalEmitted                 // skipping reduce { } or state { }
                }
            }
    }

    private suspend fun Reducer<State, Action, Effect<Proposal, SideEffect>>.runReducer(action: Action) : Boolean {
        val (proposal, sideEffects) = ReducerContext(blocState.value, action).this()
        return if (proposal != null) {
            blocState.emit(proposal)
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

}
