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

    private val events = Channel<Event>(UNLIMITED)
    private val sideEffects = Channel<SideEffect>(UNLIMITED)

    init {
        context.lifecycle.doOnCreate {
            Logger.withTag("knot").d("doOnCreate -> start Knot")
            start(context.coroutineScope)
        }
        context.lifecycle.doOnDestroy {
            Logger.withTag("knot").d("doOnDestroy -> stop Knot")
            // note: we don't need to cancel the jobs
            // they will be cancelled automatically when the parent CoroutineScope completes
        }
    }

    private var eventsJob: Job? = null
    private var sideEffectJob: Job? = null

    override val value = knotState.value

    override fun emit(value: Event) {
        Logger.withTag("knot").d("emit $value")
        events.trySend(value)
    }

    override suspend fun collect(collector: FlowCollector<State>) {
        knotState.collect(collector)
    }

    private fun start(coroutineScope: CoroutineScope) {
        eventsJob = coroutineScope.launch(dispatcherReduce) {
            for (event in events) {
                Logger.withTag("knot").d("processing event $event")
                val effect = reducer.invoke(knotState.value, event)
                knotState.emit(effect.proposal)
                effect.sideEffects.forEach { sideEffects.send(it) }
            }
            Logger.withTag("knot").d("processing event DONE")
        }

        sideEffectJob = coroutineScope.launch(dispatcherSideEffect) {
            for (sideEffect in sideEffects) {
                Logger.withTag("knot").d("processing sideEffect $sideEffect")
                when (executor) {
                    null -> throw IllegalStateException("Side effect created but no executor defined")
                    else -> executor.invoke(sideEffect)?.also { events.send(it) }
                }
            }
            Logger.withTag("knot").d("processing sideEffect DONE")
        }
    }

}
