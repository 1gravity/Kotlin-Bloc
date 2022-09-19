package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.internal.builder.MatcherReducer
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

/**
 * The ReduceProcessor is responsible for processing the reduce { }, reduceAnd { } and
 * sideEffect { } blocks.
 */
internal class ReduceProcessor<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>,
    private val reduceDispatcher: CoroutineContext = Dispatchers.Default
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

    private var coroutineScope = CoroutineScope(SupervisorJob() + reduceDispatcher)
        set(value) {
            field = value
            coroutineRunner = CoroutineRunner(coroutineScope)
        }

    private var coroutineRunner = CoroutineRunner(coroutineScope)

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        blocContext.lifecycle.doOnStart {
            logger.d("onStart -> start Bloc")
            coroutineScope = CoroutineScope(SupervisorJob() + reduceDispatcher)
            processQueue()
        }

        blocContext.lifecycle.doOnStop {
            logger.d("onStop -> stop Bloc")
            coroutineScope.cancel()
        }
    }

    private fun processQueue() {
        coroutineScope.launch {
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
        coroutineScope.run {
            mutex.withLock {
                val context = ReducerContext(blocState.value, action, coroutineRunner)
                val reduce = this@runReducer
                val (proposal, sideEffects) = context.reduce()
                proposal?.let { blocState.send(it) }
                postSideEffects(sideEffects)
            }
        }
    }

    /**
     * Triggered to execute a specific reducer (dispatched MVVM+ style)
     */
    private fun runReducer(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        coroutineScope.launch {
            mutex.withLock {
                val context = ReducerContextNoAction(blocState.value, coroutineRunner)
                val (proposal, sideEffects) = context.reduce()
                proposal?.let { blocState.send(it) }
                postSideEffects(sideEffects)
            }
        }
    }

    private suspend fun postSideEffects(sideEffects: List<SideEffect>) {
        sideEffects.forEach {
            sideEffectChannel.send(it)
        }
    }

}
