package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.onegravity.bloc.builder.Matcher
import com.onegravity.bloc.builder.MatcherThunk
import com.onegravity.bloc.context.BlocContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

class BlocImpl<State, Action: Any, Proposal>(
    context: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    thunks: List<MatcherThunk<State, Action>> = emptyList(),
    private val reducer: Reducer<State, Action, Proposal>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, Proposal> {

    data class ThunkRecord<State, Action : Any>(
        val matcher: Matcher<Action, Action>?,
        val thunk: Thunk<State, Action>,
        val dispatcher: Dispatcher<Action>
    )

    private val thunkRecords: List<ThunkRecord<State, Action>> = thunks
        .foldIndexed(ArrayList()) { index, list, value ->
            ThunkRecord(value.matcher, value.thunk, getDispatcher(index))
            list
        }

    private val reduceDispatcher: Dispatcher<Action> = { action ->
        val proposal = reducer.invoke(blocState.value, action)
        blocState.emit(proposal)
    }

    private fun getDispatcher(index: Int): Dispatcher<Action> =
        when (index == thunkRecords.lastIndex) {
            true -> reduceDispatcher
            else -> { action ->
                val (_, thunk, _) = thunkRecords[index + 1]
                thunk.invoke({ blocState.value }, action, getDispatcher(index + 1))
            }
        }

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

        if (thunkRecords.any { (matcher, _, _) -> matcher == null || matcher.matches(action) }) {
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
                thunkRecords.forEachIndexed { index, (matcher, thunk, _) ->
                    if (matcher == null || matcher.matches(action)) {
                        val nextDispatcher = nextDispatcher(index + 1, action)
                        thunk.invoke({ blocState.value }, action, nextDispatcher)
                    }
                }
            }
        }

    }

    private fun nextDispatcher(startIndex: Int, action: Action): Dispatcher<Action> {
        (startIndex..thunkRecords.lastIndex).forEach { index ->
            val (matcher, _, dispatcher) = thunkRecords[index]
            if (matcher == null || matcher.matches(action)) {
                return dispatcher
            }
        }
        return reduceDispatcher
    }

}