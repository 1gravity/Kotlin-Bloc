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
    private val reducer: Reducer<State, Action, Proposal>,
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

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun emit(action: Action) {
        logger.d("emit action $action")
        if (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            actions.trySend(action)
        } else {
            val proposal = reducer.invoke(blocState.value, action)
            blocState.emit(proposal)
        }
    }

    override suspend fun collect(collector: FlowCollector<State>) {
        blocState.collect(collector)
    }

    private fun CoroutineScope.start() = launch(dispatcher) {
        for (action in actions) {
            processActions(action)
        }
    }

    private suspend fun processActions(action: Action) {
        logger.d("process action $action")
        thunks.forEachIndexed { index, (matcher, _) ->
            if (matcher == null || matcher.matches(action)) {
                executeThunk(index, action)
            }
        }
    }

    private suspend fun executeThunk(index: Int, action: Action) {
        val dispatcher: Dispatcher<Action> = {
            nextDispatcher(index + 1, it).invoke(it)
        }
        thunks[index].thunk.invoke({ blocState.value }, action, dispatcher)
    }

    private fun nextDispatcher(startIndex: Int, action: Action): Dispatcher<Action> {
        (startIndex..thunks.lastIndex).forEach { index ->
            val matcher = thunks[index].matcher
            if (matcher == null || matcher.matches(action)) {
                return { executeThunk(index, action) }
            }
        }

        // Dispatcher for reduce { }
        return {
            val proposal = reducer.invoke(blocState.value, action)
            blocState.emit(proposal)
        }
    }

}