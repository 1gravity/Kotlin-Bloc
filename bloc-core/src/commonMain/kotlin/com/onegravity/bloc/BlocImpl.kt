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
import kotlin.coroutines.CoroutineContext

class BlocImpl<State, Action: Any, Proposal>(
    context: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val thunks: List<MatcherThunk<State, Action>> = emptyList(),
    private val reducers: List<MatcherReducer<State, Action, Proposal>> = emptyList(),
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, Proposal> {

    private val actions = Channel<Action>(UNLIMITED)

    init {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        context.lifecycle.doOnCreate {
            logger.d("onCreate -> start Bloc")
            coroutineScope.start()
        }

        context.lifecycle.doOnDestroy {
            logger.d("onDestroy -> stop Bloc")
            coroutineScope.cancel("Stop Bloc")
        }
    }

    override val value
        get() = blocState.value

    override val replayCache: List<State>
        get() = blocState.replayCache

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun emit(action: Action) {
        logger.d("emit action $action")
        if (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            actions.trySend(action)
        } else {
            getReducer(action)?.runReducer(action)
        }
    }

    override suspend fun collect(collector: FlowCollector<State>): Nothing {
        blocState.collect(collector)
    }

//    override val sideEffects: Stream<SideEffect>
//        get() = TODO("Not yet implemented")

    private fun CoroutineScope.start() = launch(dispatcher) {
        for (action in actions) {
            processActions(action)
        }
    }

    private suspend fun processActions(action: Action) {
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

    private fun Reducer<State, Action, Proposal>.runReducer(action: Action) {
        val proposal = ReducerContext(blocState.value, action).this()
        blocState.emit(proposal)
    }

    private fun nextThunkDispatcher(startIndex: Int, action: Action): Dispatcher<Action> {
        (startIndex..thunks.lastIndex).forEach { index ->
            val matcher = thunks[index].matcher
            if (matcher == null || matcher.matches(action)) {
                return { runThunk(index, action) }
            }
        }

        return { getReducer(action)?.runReducer(action) }
    }

    private fun getReducer(action: Action): Reducer<State, Action, Proposal>? {
        val reducer = reducers
            .firstOrNull { it.matcher == null || it.matcher.matches(action) }
            ?.reducer
        if (reducer == null) {
            logger.e("No reducer found, did you define reduce { }?")
        }
        return reducer
    }

}
