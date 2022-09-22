package com.onegravity.bloc.internal

import com.onegravity.bloc.internal.builder.MatcherReducer
import com.onegravity.bloc.internal.lifecycle.BlocLifecycle
import com.onegravity.bloc.internal.lifecycle.subscribe
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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
     * Mutex to ensure only one reducer runs at a time.
     */
    private val mutex = Mutex()

    /**
     * Channel for reducers to be processed (incoming)
     */
    private val reduceChannel = Channel<ReduceChannelElement<State, Action, SideEffect, Proposal>>(UNLIMITED)

    /**
     * Channel for side effects (outgoing)
     */
    private val sideEffectChannel = Channel<SideEffect>(UNLIMITED)

    /**
     * Flow for side effects (outgoing)
     */
    internal val sideEffects: SideEffectStream<SideEffect> = sideEffectChannel.receiveAsFlow()

    private var coroutine: Coroutine = Coroutine(dispatcher)

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        lifecycle.subscribe(
            onStart = {
                logger.d("onStart -> start Bloc")
                coroutine.onStart()
                processQueue()
            },
            onStop = {
                logger.d("onStop -> stop Bloc")
                coroutine.onStop()
            }
        )
    }

    private fun processQueue() {
        coroutine.scope?.launch {
            for (element in reduceChannel) {
                element.action?.let { action ->
                    runReducers(action)
                }
                element.reducer?.let { reduce ->
                    runReducer(reduce)
                }
            }
        }
    }

    /**
     * BlocDSL:
     * reduce { } -> run a Reducer Redux style
     */
    internal fun send(action: Action) {
        reduceChannel.trySend(ReduceChannelElement(action))
    }

    /**
     * BlocExtension interface implementation:
     * reduce { } -> run a Reducer MVVM+ style
     */
    internal fun reduce(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        reduceChannel.trySend(ReduceChannelElement(reducer = reduce))
    }

    /**
     * Triggered to execute reducers with a matching Action
     */
    private suspend fun runReducers(action: Action) {
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
    private suspend fun Reducer<State, Action, Effect<Proposal, SideEffect>>.runReducer(
        action: Action
    ) {
        coroutine.scope?.run {
            mutex.withLock {
                coroutine.runner?.let { runner ->
                    val context = ReducerContext(state.value, action, runner)
                    val reduce = this@runReducer
                    val (proposal, sideEffects) = context.reduce()
                    proposal?.let { state.send(it) }
                    postSideEffects(sideEffects)
                }
            }
        }
    }

    /**
     * Triggered to execute a specific reducer (dispatched MVVM+ style)
     */
    private fun runReducer(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        coroutine.scope?.launch {
            coroutine.runner?.let { runner ->
                mutex.withLock {
                    val context = ReducerContextNoAction(state.value, runner)
                    val (proposal, sideEffects) = context.reduce()
                    proposal?.let { state.send(it) }
                    postSideEffects(sideEffects)
                }
            }
        }
    }

    private suspend fun postSideEffects(sideEffects: List<SideEffect>) {
        sideEffects.forEach {
            sideEffectChannel.send(it)
        }
    }

}
