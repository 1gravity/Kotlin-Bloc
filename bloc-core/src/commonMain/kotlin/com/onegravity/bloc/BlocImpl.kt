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

/**
 * todo reduce { } should use its own dispatcher, right now it's running on the thunk dispatcher
 */
class BlocImpl<State, Action: Any, Proposal>(
    context: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val thunks: List<MatcherThunk<State, Action>> = emptyList(),
    private val reducer: Reducer<State, Action, Proposal>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, Proposal> {

    private val actions = Channel<Action>(UNLIMITED)

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    init {

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
        actions.trySend(action)
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
        if (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            thunks.forEachIndexed { index, (matcher, _) ->
                if (matcher == null || matcher.matches(action)) {
                    executeThunk(index, action)
                }
            }
        } else {
            reduceDispatcher.invoke(action)
        }
    }

    private val reduceDispatcher: Dispatcher<Action> = { action ->
        val proposal = reducer.invoke(blocState.value, action)
        blocState.emit(proposal)
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
        return reduceDispatcher
    }

}