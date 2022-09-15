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
import kotlin.coroutines.CoroutineContext

/**
 * The ThunkProcessor is responsible for processing thunk { } blocks.
 */
internal class ThunkProcessor<State : Any, Action : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val thunks: List<MatcherThunk<State, Action, Action>> = emptyList(),
    private val thunkDispatcher: CoroutineContext = Dispatchers.Default,
    private val runReducers: (action: Action) -> Unit
) {

    /**
     * Channel for thunks to be processed (incoming)
     */
    private val thunkChannel = Channel<Action>(UNLIMITED)

    private var scope: CoroutineScope? = null

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
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

    private var job: Job? = null

    private fun thunkContext(
        getState: GetState<State>,
        dispatcher: Dispatcher<Action>,
        coroutineScope: CoroutineScope,
    ) : ThunkContextNoAction<State, Action> {

        val cancelBeforeLaunch: (
            cancelBeforeLaunch: Boolean,
            block: suspend CoroutineScope.() -> Unit
        ) -> Job = { cancelBeforeLaunch, block ->
            if (cancelBeforeLaunch) job?.cancel()
            coroutineScope
                .launch { block() }
                .also { job = it }
        }

        return ThunkContextNoAction(
            getState = getState,
            dispatch = dispatcher,
            coroutineScope = coroutineScope,
            launch = cancelBeforeLaunch
        )
    }

    /**
     * BlocExtension interface implementation:
     * thunk { } -> run a thunk MVVM+ style
     */
    internal fun thunk(thunk: ThunkNoAction<State, Action>) =
        scope?.launch {
            val dispatcher: Dispatcher<Action> = {
                nextThunkDispatcher(it).invoke(it)
            }
            val context = thunkContext(
                getState = { blocState.value },
                dispatcher = dispatcher,
                coroutineScope = this
            )
            context.thunk()
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
        scope?.run {
            val dispatcher: Dispatcher<Action> = {
                nextThunkDispatcher(it, index + 1).invoke(it)
            }
            val thunk = thunks[index].thunk
            val context = ThunkContext(
                getState = { blocState.value },
                action = action,
                dispatch = dispatcher,
                coroutineScope = this
            )
            context.thunk()
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
