package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.internal.builder.MatcherThunk
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

/**
 * The ThunkProcessor is responsible for processing thunk { } blocks.
 */
internal class ThunkProcessor<State : Any, Action : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val thunks: List<MatcherThunk<State, Action, Action>> = emptyList(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val runReducers: (action: Action) -> Unit
) {

    /**
     * Channel for thunks to be processed (incoming)
     */
    private val thunkChannel = Channel<Action>(UNLIMITED)

    private var coroutine: Coroutine = Coroutine(dispatcher)

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        blocContext.lifecycle.doOnStart {
            coroutine.onStart()
            processQueue()
        }

        blocContext.lifecycle.doOnStop {
            coroutine.onStop()
        }
    }

    private fun processQueue() {
        coroutine.scope?.launch {
            for (action in thunkChannel) {
                runThunks(action)
            }
        }
    }

    /**
     * BlocDSL:
     * thunk { } -> run a thunk Redux style
     */
    internal fun send(action: Action) {
        if (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            thunkChannel.trySend(action)
        } else {
            runReducers(action)
        }
    }

    /**
     * BlocExtension interface implementation:
     * thunk { } -> run a thunk MVVM+ style
     */
    internal fun thunk(thunk: ThunkNoAction<State, Action>) =
        coroutine.scope?.launch {
            coroutine.runner?.let { runner ->
                val dispatcher: Dispatcher<Action> = {
                    nextThunkDispatcher(it).invoke(it)
                }
                val context = ThunkContextNoAction(
                    getState = { blocState.value },
                    dispatch = dispatcher,
                    runner = runner
                )
                context.thunk()
            }
        }

    /**
     * Run all matching thunks
     *
     * @param action run thunks that match this action
     * @param thunkIndex start executing thunks from thunkIndex on
     */
    private suspend fun runThunks(action: Action, thunkIndex: Int = 0) {
        logger.d("run thunks for action ${action.trimOutput()}")
        (thunkIndex..thunks.lastIndex).forEach { index ->
            val (matcher, _) = thunks[index]
            if (matcher == null || matcher.matches(action)) {
                runThunk(action, index)
            }
        }
    }

    /**
     * Run a specific thunk
     */
    private suspend fun runThunk(action: Action, index: Int) {
        coroutine.scope.run {
            coroutine.runner?.let { runner ->
                val dispatcher: Dispatcher<Action> = {
                    nextThunkDispatcher(it, index + 1).invoke(it)
                }
                val thunk = thunks[index].thunk
                val context = ThunkContext(
                    getState = { blocState.value },
                    action = action,
                    dispatch = dispatcher,
                    runner = runner
                )
                context.thunk()
            }
        }
    }

    /**
     * Find the next thunk to execute (when dispatch is used)
     */
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
