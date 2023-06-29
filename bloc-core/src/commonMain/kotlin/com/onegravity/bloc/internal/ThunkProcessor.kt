package com.onegravity.bloc.internal

import com.onegravity.bloc.internal.builder.MatcherThunk
import com.onegravity.bloc.internal.lifecycle.BlocLifecycle
import com.onegravity.bloc.internal.lifecycle.subscribe
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.Dispatcher
import com.onegravity.bloc.utils.ThunkContext
import com.onegravity.bloc.utils.ThunkContextNoAction
import com.onegravity.bloc.utils.ThunkNoAction
import com.onegravity.bloc.utils.logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

/**
 * The ThunkProcessor is responsible for processing thunk { } blocks.
 */
internal class ThunkProcessor<State : Any, Action : Any, Proposal : Any>(
    lifecycle: BlocLifecycle,
    private val state: BlocState<State, Proposal>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val thunks: List<MatcherThunk<State, Action, Action, Proposal>> = emptyList(),
    private val dispatch: suspend (action: Action) -> Unit,
    private val reduce: suspend (proposal: Proposal) -> Unit
) {

    /**
     * Channel for thunks to be processed (incoming)
     */
    private val thunkChannel = Channel<Action>(UNLIMITED)

    private var coroutineHelper: CoroutineHelper = CoroutineHelper(dispatcher)

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        lifecycle.subscribe(
            onInitialize = {
                startProcessing()
            },
            onStart = {
                startProcessing()
            },
            onStop = {
                coroutineHelper.onStop()
            }
        )
    }
    private fun startProcessing() {
        // only start processing if the coroutine wasn't already started
        if (!coroutineHelper.onStart()) return

        coroutineHelper.launch {
            for (action in thunkChannel) {
                runThunks(action)
            }
        }
    }

    /**
     * BlocDSL:
     * thunk { } -> run a thunk Redux style
     */
    internal fun send(action: Action): Boolean {
        logger.d("received thunk with action ${action.trimOutput()}")
        if (thunks.any { it.matcher == null || it.matcher.matches(action) }) {
            thunkChannel.trySend(action)
            return true
        }
        return false
    }

    /**
     * BlocExtension interface implementation:
     * thunk { } -> run a thunk MVVM+ style
     */
    internal fun thunk(thunk: ThunkNoAction<State, Action, Proposal>) {
        // we don't need to check if (lifecycle.state == LifecycleState.Started) since the
        // CoroutineScope is cancelled onStop()
        coroutineHelper.launch {
            logger.d("received thunk without action")
            val dispatcher: Dispatcher<Action> = {
                nextThunkDispatcher(it).invoke(it)
            }
            val context = ThunkContextNoAction(
                getState = { state.value },
                dispatch = dispatcher,
                reduce = reduce,
                launchBlock = coroutineHelper::launch
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
        val dispatcher: Dispatcher<Action> = {
            nextThunkDispatcher(it, index + 1).invoke(it)
        }
        val thunk = thunks[index].thunk
        val context = ThunkContext(
            getState = { state.value },
            action = action,
            dispatch = dispatcher,
            reduce = reduce,
            launchBlock = coroutineHelper::launch
        )
        context.thunk()
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

        return { dispatch(action) }
    }

}
