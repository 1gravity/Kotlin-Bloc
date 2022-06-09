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

internal class ReduceProcessor<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val reducers: List<MatcherReducer<State, Action, Effect<Proposal, SideEffect>>>,
    private val reduceDispatcher: CoroutineContext = Dispatchers.Default
) {

    private val reduceMutex = Mutex()
    private val reduceChannel = Channel<ReduceChannelElement<State, Action, SideEffect, Proposal>>(UNLIMITED)

    private val sideEffectChannel = Channel<SideEffect>(UNLIMITED)

    private var scope: CoroutineScope? = null

    internal val sideEffects: SideEffectStream<SideEffect> = sideEffectChannel.receiveAsFlow()

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        blocContext.lifecycle.doOnStart {
            logger.d("onStart -> start Bloc")
            scope = CoroutineScope(SupervisorJob() + reduceDispatcher)
            processQueue()
        }

        blocContext.lifecycle.doOnStop {
            logger.d("onStop -> stop Bloc")
            scope?.cancel()
        }
    }

    private fun processQueue() {
        scope?.launch {
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

    fun send(action: Action) {
        reduceChannel.trySend(ReduceChannelElement(action))
    }

    internal fun reduce(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        reduceChannel.trySend(ReduceChannelElement(reducer = reduce))
    }

    internal suspend fun runReducers(action: Action) {
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

    private suspend fun Reducer<State, Action, Effect<Proposal, SideEffect>>.runReducer(
        action: Action
    ) {
        scope?.run {
            reduceMutex.withLock {
                val context = ReducerContext(blocState.value, action, this)
                val reduce = this@runReducer
                val (proposal, sideEffects) = context.reduce()
                proposal?.let { blocState.send(it) }
                postSideEffects(sideEffects)
            }
        }
    }

    private fun runReducer(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>) {
        scope?.launch {
            reduceMutex.withLock {
                val context = ReducerContextNoAction(blocState.value, this)
                val (proposal, sideEffects) = context.reduce()
                proposal?.let { blocState.send(it) }
                postSideEffects(sideEffects)
            }
        }
    }

    private fun getMatchingReducers(action: Action) = reducers
        .filter { it.matcher == null || it.matcher.matches(action) }
        .also {
            if (it.isEmpty()) logger.e("No reducer found, did you define reduce { }?")
        }

    private suspend fun postSideEffects(sideEffects: List<SideEffect>) {
        sideEffects.forEach {
            sideEffectChannel.send(it)
        }
    }

}
