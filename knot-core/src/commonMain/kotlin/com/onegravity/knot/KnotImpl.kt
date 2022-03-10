package com.onegravity.knot

import co.touchlab.kermit.Logger
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.KnotState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

class KnotImpl<State, Event, Proposal, SideEffect>(
    private val context: KnotContext,
    private val knotState: KnotState<State, Proposal>,
    private val reducer: Reducer<State, Event, Proposal, SideEffect>,
    private val executor: Executor<SideEffect, Event>?,
    private val dispatcherReduce: CoroutineContext = Dispatchers.Default,
    private val dispatcherSideEffect: CoroutineContext = Dispatchers.Default
) : Knot<State, Event, Proposal, SideEffect> {
    init {
        context.lifecycle.doOnCreate {
            Logger.withTag("knot").i("doOnCreate -> start Knot")
            start(context.coroutineScope)
        }
        context.lifecycle.doOnDestroy {
            Logger.withTag("knot").i("doOnDestroy -> stop Knot")
            stop()
        }
    }

    private val events = Channel<Event>(UNLIMITED)
    private val sideEffects = Channel<SideEffect>(UNLIMITED)

    private var eventsJob: Job? = null
    private var sideEffectJob: Job? = null

    override val value: State
        get() = knotState.value

    override fun emit(value: Event) {
        Logger.withTag("knot").i("emit $value / ${this.hashCode()}")
        events.trySend(value)
    }

    override suspend fun collect(collector: FlowCollector<State>) {
        knotState.collect(collector)
    }

    private fun start(coroutineScope: CoroutineScope) {
        stop()

        eventsJob = coroutineScope.launch(dispatcherReduce) {
            for (event in events) {
                Logger.withTag("knot").i("processing event $event")
                val effect = reducer.invoke(knotState.value, event)
                knotState.emit(effect.proposal)
                effect.sideEffects.forEach { sideEffects.send(it) }
            }
        }

        sideEffectJob = coroutineScope.launch(dispatcherSideEffect) {
            for (sideEffect in sideEffects) {
                Logger.withTag("knot").i("processing sideEffect $sideEffect")
                when (executor) {
                    null -> throw IllegalStateException("Side effect created but no executor defined")
                    else -> executor.invoke(sideEffect)?.also { events.send(it) }
                }
            }
        }
    }

    private fun stop() {
        eventsJob?.cancel("")?.also { eventsJob = null }
        sideEffectJob?.cancel("")?.also { sideEffectJob = null }
    }

}
