package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.onegravity.bloc.context.BlocContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

class BlocImpl<State, Action: Any, Proposal>(
    context: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val thunks: List<Thunk<State, Action>> = emptyList(),
    private val actionThunks: Map<Matcher<Action, Action>, Thunk<State, Action>> = emptyMap(),
    private val reducer: Reducer<State, Action, Proposal>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, Proposal> {

    private val actions = Channel<Action>(UNLIMITED)

    init {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        context.lifecycle.doOnCreate {
            logger.d("onCreate -> start Bloc")
            start(coroutineScope)
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

        if (actionThunks.any { it.key.matches(action) } || thunks.isNotEmpty()) {
            actions.trySend(action)
        } else {
            val proposal = reducer.invoke(blocState.value, action)
            blocState.emit(proposal)
        }
    }

    override suspend fun collect(collector: FlowCollector<State>) {
        blocState.collect(collector)
    }

    private fun start(coroutineScope: CoroutineScope) {
        coroutineScope.launch(dispatcher) {
            for (action in actions) {
                logger.d("process action $action")
                val reduceDispatcher: Dispatcher<Action> = { reducerAction ->
                    val proposal = reducer.invoke(blocState.value, reducerAction)
                    blocState.emit(proposal)
                }
                actionThunks
                    .filter { it.key.matches(action) }
                    .forEach { (_, thunk) ->
                        thunk.invoke({ blocState.value }, action, reduceDispatcher)
                    }
                thunks.forEach { thunk ->
                    thunk.invoke( { blocState.value }, action, reduceDispatcher)
                }
            }
        }
    }

}
