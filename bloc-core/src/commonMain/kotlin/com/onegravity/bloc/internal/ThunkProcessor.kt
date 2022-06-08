package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlin.coroutines.CoroutineContext

internal class ThunkProcessor<State : Any, Action : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val thunks: List<MatcherThunk<State, Action, Action>> = emptyList(),
    private val thunkDispatcher: CoroutineContext = Dispatchers.Default,
    private val runReducers: suspend (action: Action) -> Unit
) {

    private val thunkChannel = Channel<Action>(UNLIMITED)

    private var scope: CoroutineScope? = null

    init {
        blocContext.lifecycle.doOnStart {
            scope = CoroutineScope(SupervisorJob() + thunkDispatcher)
            processQueue()
        }

        blocContext.lifecycle.doOnStop {
            scope?.cancel()
        }
    }

    private fun processQueue() {
        scope?.launch {
            for (action in thunkChannel) {
                runThunks(action)
            }
        }
    }

    /**
     * @return True if the action is being picked up by a thunk, False otherwise
     */
    internal fun send(action: Action) : Boolean =
        if (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            thunkChannel.trySend(action)
            true
        } else {
            false
        }

    /**
     * Public API (interface BlocExtension) to run thunks / reducers etc. MVVM+ style
     */
    internal fun thunk(thunk: ThunkNoAction<State, Action>) =
        scope?.launch {
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

    private suspend fun runThunks(action: Action, startIndex: Int = 0) {
        logger.d("run thunks for action ${action.trimOutput()}")
        (startIndex..thunks.lastIndex).forEach { index ->
            val (matcher, _) = thunks[index]
            if (matcher == null || matcher.matches(action)) {
                runThunk(action, index)
            }
        }
    }

    private suspend fun runThunk(action: Action, index: Int) {
        scope?.run {
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

}
