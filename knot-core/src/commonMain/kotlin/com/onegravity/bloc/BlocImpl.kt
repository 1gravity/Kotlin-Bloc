package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.onegravity.bloc.context.BlocContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

class BlocImpl<State, Action, Proposal>(
    context: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val thunks: List<Thunk<State, Action>> = emptyList(),
    @Suppress("UNCHECKED_CAST")
    private val reducer: Reducer<State, Action, Proposal> = { _, action -> action as Proposal },
    private val dispatcherThunks: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, Proposal> {

    private val actions = Channel<Action>(UNLIMITED)

    private val thunkDispatchers = thunks.indices
        .reversed()
        .fold(ArrayList<ThunkDispatcher<State, Action>>()) { list, index ->
            val dispatcher: Dispatcher<Action> =
                when (index == thunks.lastIndex) {
                    true -> { action ->
                        val proposal = reducer.invoke(blocState.value, action)
                        blocState.emit(proposal)
                    }
                    else -> { action ->
                        val (thunk, dispatcher) = list[index + 1]
                        thunk.invoke(blocState.value, action, dispatcher)
                    }
                }
            list.add(0, ThunkDispatcher(thunks[index], dispatcher))
            list
        }

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

    override val value = blocState.value

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun emit(action: Action) {
        logger.d("emit $action")
        if (thunks.isNotEmpty()) {
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

        coroutineScope.launch(dispatcherThunks) {
            for (action in actions) {
                logger.d("processing action $action")
                thunkDispatchers.firstOrNull()?.let { (thunk, dispatcher) ->
                        thunk.invoke(blocState.value, action, dispatcher)
                }
            }
        }
    }

}
