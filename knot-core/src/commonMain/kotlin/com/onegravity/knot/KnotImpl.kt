package com.onegravity.knot

import co.touchlab.kermit.Logger
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.BlocState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

class KnotImpl<State, Event, Proposal, SideEffect>(
    context: BlocContext,
    private val knotState: BlocState<State, Proposal>,
    private val reducer: Reducer<State, Event, Proposal, SideEffect>,
    private val executor: Executor<SideEffect, Event>?,
    private val dispatcherReduce: CoroutineContext = Dispatchers.Default,
    private val dispatcherSideEffect: CoroutineContext = Dispatchers.Default
) : Bloc<State, Event, Proposal> {

    private val events = Channel<Event>(UNLIMITED)
    private val sideEffects = Channel<SideEffect>(UNLIMITED)

    init {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        context.lifecycle.doOnCreate {
            Logger.withTag("knot").d("onCreate -> start Knot")
            start(coroutineScope)
        }
        context.lifecycle.doOnDestroy {
            Logger.withTag("knot").d("onDestroy -> stop Knot")
            coroutineScope.cancel("Stop Knot")
        }
    }

    override val value = knotState.value

    override fun emit(value: Event) {
        Logger.withTag("knot").d("emit $value")
        events.trySend(value)
    }

    override suspend fun collect(collector: FlowCollector<State>) {
        knotState.collect(collector)
    }

    private fun start(coroutineScope: CoroutineScope) {
        coroutineScope.launch(dispatcherReduce) {
            for (event in events) {
                Logger.withTag("knot").d("processing event $event")
                val effect = reducer.invoke(knotState.value, event)
                knotState.emit(effect.proposal)
                effect.sideEffects.forEach { sideEffects.send(it) }
            }
        }

        coroutineScope.launch(dispatcherSideEffect) {
            for (sideEffect in sideEffects) {
                Logger.withTag("knot").d("processing sideEffect $sideEffect")
                when (executor) {
                    null -> throw IllegalStateException("Side effect created but no executor defined")
                    else -> executor.invoke(sideEffect)?.also { events.send(it) }
                }
            }
        }
    }

}
