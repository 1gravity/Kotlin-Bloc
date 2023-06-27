package com.onegravity.bloc.internal

import com.onegravity.bloc.internal.builder.MatcherReducer
import com.onegravity.bloc.internal.lifecycle.BlocLifecycle
import com.onegravity.bloc.internal.lifecycle.subscribe
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.Effect
import com.onegravity.bloc.utils.Reducer
import com.onegravity.bloc.utils.ReducerContext
import com.onegravity.bloc.utils.ReducerContextNoAction
import com.onegravity.bloc.utils.ReducerNoAction
import com.onegravity.bloc.utils.SideEffectStream
import com.onegravity.bloc.utils.logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * The ReduceProcessor is responsible for processing the reduce { }, reduceAnd { } and
 * sideEffect { } blocks.
 */
internal class ReduceProcessor<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    lifecycle: BlocLifecycle,
    private val state: BlocState<State, Proposal>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>
) {

    /**
     * Channel for side effects (outgoing)
     */
    private val sideEffectChannel = Channel<SideEffect>(UNLIMITED)

    /**
     * Flow for side effects (outgoing)
     */
    internal val sideEffects: SideEffectStream<SideEffect> = sideEffectChannel.receiveAsFlow()

    private var coroutineHelper: CoroutineHelper = CoroutineHelper(dispatcher)

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        lifecycle.subscribe(
            onStart = {
                logger.d("onStart -> start Bloc")
            },
            onStop = {
                logger.d("onStop -> stop Bloc")
                // stopping
                coroutineHelper.onStop()
            }
        )
    }

    /**
     * BlocDSL:
     * reduce { } -> run a Reducer Redux style
     */
    internal fun send(action: Action) {
        logger.d("trigger reducers with action ${action.trimOutput()}")
        runReducers(action)
    }

    /**
     * BlocExtension interface implementation:
     * reduce { } -> run a Reducer MVVM+ style
     */
    internal fun reduce(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        logger.d("received reducer without action")
        runReducer(reduce)
    }

    /**
     * Triggered to execute reducers with a matching Action
     */
    private fun runReducers(action: Action) {
        logger.d("run reducers for action ${action.trimOutput()}")
        getMatchingReducers(action).fold(false) { proposalEmitted, matcherReducer ->
            val (_, reducer, expectsProposal) = matcherReducer
            when {
                // running sideEffect { } -> always run no matter what
                !expectsProposal -> {
                    reducer.runReducer(action)
                    proposalEmitted
                }

                // running reduce { } or reduceAnd { }
                // -> only run if state wasn't reduced already (proposalEmitted = true)
                !proposalEmitted -> {
                    reducer.runReducer(action)
                    true
                }

                // reduce { } or reduceAnd { } but state was already reduced (proposalEmitted = true)
                else -> proposalEmitted
            }
        }
    }

    private fun getMatchingReducers(action: Action) = reducers
        .filter { it.matcher == null || it.matcher.matches(action) }
        .also {
            if (it.isEmpty()) logger.e("No reducer found, did you define reduce { }?")
        }

    /**
     * Triggered to execute a specific reducer (dispatched Redux style)
     */
    private fun Reducer<State, Action, Effect<Proposal, SideEffect>>.runReducer(action: Action) {
        val context = ReducerContext(state.value, action, coroutineHelper::launch)
        val reduce = this@runReducer
        val (proposal, sideEffects) = context.reduce()
        proposal?.let(state::send)
        sideEffects.forEach(sideEffectChannel::trySend)
    }

    /**
     * Triggered to execute a specific reducer (dispatched MVVM+ style)
     */
    private fun runReducer(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        val context = ReducerContextNoAction(state.value, coroutineHelper::launch)
        val (proposal, sideEffects) = context.reduce()
        proposal?.let(state::send)
        sideEffects.forEach(sideEffectChannel::trySend)
    }

}
