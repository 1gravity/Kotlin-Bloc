package com.onegravity.knot

import com.onegravity.knot.state.KnotState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

class KnotImpl<State, Event, Proposal, SideEffect>(
    private val knotState: KnotState<State, Proposal>,
    private val reducer: Reducer<State, Event, Proposal, SideEffect>?,
    private val suspendReducer: SuspendReducer<State, Event, Proposal, SideEffect>?,
    private val executor: Executor<SideEffect, Event>?,
    private val suspendExecutor: SuspendExecutor<SideEffect, Event>?,
    private val dispatcherReduce: CoroutineContext = Dispatchers.Default,
    private val dispatcherSideEffect: CoroutineContext = Dispatchers.Default
) : Knot<State, Event, Proposal, SideEffect>,
    JobSwitcher
{

    private val events = Channel<Event>(UNLIMITED)
    private val sideEffects = Channel<SideEffect>(UNLIMITED)

    private var eventsJob: Job? = null
    private var sideEffectJob: Job? = null

    override val value: State
        get() = knotState.value

    override fun emit(value: Event) {
        events.trySend(value)
    }

    override suspend fun collect(collector: FlowCollector<State>): Nothing {
        knotState.collect(collector)
    }

    override fun start(coroutineScope: CoroutineScope) {
        stop()

        eventsJob = coroutineScope.launch(dispatcherReduce) {
            for (action in events) {
                val effect = reducer?.invoke(knotState.value, action) ?: suspendReducer?.invoke(knotState.value, action)
                effect?.also { effect ->
                    knotState.emit(effect.proposal)
                    effect.sideEffects.forEach { sideEffects.send(it) }
                }
            }
        }

        sideEffectJob = coroutineScope.launch(dispatcherSideEffect) {
            for (sideEffect in sideEffects) {
                val action = executor?.invoke(sideEffect) ?: suspendExecutor?.invoke(sideEffect)
                action?.run { events.send(action) }
            }
        }
    }

    override fun stop() {
        eventsJob?.cancel("")?.also { eventsJob = null }
        sideEffectJob?.cancel("")?.also { sideEffectJob = null }
    }

}
