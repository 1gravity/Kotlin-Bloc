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
 *      when a thunk dispatches an action or the main thread if the action is triggered by the ui
 */
class BlocImpl<State, Action: Any, Proposal>(
    context: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val thunks: List<MatcherThunk<State, Action>> = emptyList(),
    private val reducer: Reducer<State, Action, Proposal>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Bloc<State, Action, Proposal> {

    data class ThunkRecord<State, Action : Any>(
        val matcher: Matcher<Action, Action>?,
        val thunk: Thunk<State, Action>,
        val dispatcher: Dispatcher<Action>
    )

    private val reduceDispatcher: Dispatcher<Action> = { action ->
        val proposal = reducer.invoke(blocState.value, action)
        blocState.emit(proposal)
    }

    private val thunkRecords = thunks.indices
        .reversed()
        .fold(ArrayList<ThunkRecord<State, Action>>()) { list, index ->
            val (matcher, thunk) = thunks[index]
            val dispatcher: Dispatcher<Action> =
                when (index == thunks.lastIndex) {
                    true -> reduceDispatcher
                    else -> { action ->
                        val dispatcher = list[index + 1].dispatcher
                        thunks[index + 1].thunk.invoke( { blocState.value }, action, dispatcher)
                    }
                }
            list.add(0, ThunkRecord(matcher, thunk, dispatcher))
            list
        }

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
        if (thunkRecords.any { it.matcher == null || it.matcher.matches(action) }) {
            thunkRecords.forEachIndexed { index, (matcher, thunk, _) ->
                if (matcher == null || matcher.matches(action)) {
                    val dispatcher = nextDispatcher(index, action)
                    thunk.invoke({ blocState.value }, action, dispatcher)
                }
            }
        } else {
            val proposal = reducer.invoke(blocState.value, action)
            blocState.emit(proposal)
        }
    }

    private fun nextDispatcher(startIndex: Int, action: Action): Dispatcher<Action> {
        (startIndex until thunkRecords.lastIndex).forEach { index ->
            // we use the current dispatcher ([index]) if the next thunk matches ([index + 1])
            val matcher = thunkRecords[index + 1].matcher
            if (matcher == null || matcher.matches(action)) {
                return thunkRecords[index].dispatcher
            }
        }
        return reduceDispatcher
    }

}