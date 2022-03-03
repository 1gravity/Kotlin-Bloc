package com.onegravity.knot

import com.onegravity.knot.state.SimpleKnotState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

class SimpleKnotImpl<State, in Action, SideEffect>(
    private val knotState: SimpleKnotState<State, SideEffect>,
    private val reducer: Reducer<State, Action, State, SideEffect>,
    private val performer: Performer<SideEffect, Action>?,
    private val suspendPerformer: SuspendPerformer<SideEffect, Action>?,
    private val dispatcherReduce: CoroutineContext = Dispatchers.Default,
    private val dispatcherSideEffect: CoroutineContext = Dispatchers.Default
) : Knot<State, Action, State, SideEffect>,
    JobSwitcher
{

    private val actions = Channel<Action>(UNLIMITED)
    private val sideEffects = Channel<SideEffect>(UNLIMITED)

    private var actionsJob: Job? = null
    private var sideEffectJob: Job? = null

    override val value: State
        get() = knotState.value

    override fun emit(value: Action) {
        actions.trySend(value)
    }

    override suspend fun collect(collector: FlowCollector<State>): Nothing {
        knotState.collect(collector)
    }

    override fun start(coroutineScope: CoroutineScope) {
        stop()

        actionsJob = coroutineScope.launch(dispatcherReduce) {
            for (action in actions) {
                val effect = reducer(knotState.value, action)
                knotState.emit(effect)
                effect.sideEffects.forEach { sideEffects.send(it) }
            }
        }

        sideEffectJob = coroutineScope.launch(dispatcherSideEffect) {
            for (sideEffect in sideEffects) {
                val action = performer?.invoke(sideEffect) ?: suspendPerformer?.invoke(sideEffect)
                action?.run { actions.send(action) }
            }
        }
    }

    override fun stop() {
        actionsJob?.cancel("")?.also { actionsJob = null }
        sideEffectJob?.cancel("")?.also { sideEffectJob = null }
    }

}