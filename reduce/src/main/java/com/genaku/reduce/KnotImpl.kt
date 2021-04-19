package com.genaku.reduce

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

class KnotImpl<S : State, C : StateIntent, A : StateAction>(
    private val knotState: CoroutineKnotState<S>,
    private val reducer: Reducer<S, C, A>,
    private val performer: Performer<A, C>?,
    private val suspendPerformer: SuspendPerformer<A, C>?,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<S, C>, KnotState<S> by knotState {

    private val _running = AtomicBoolean(false)

    private val _intentsChannel = Channel<C>(UNLIMITED)
    private val _actionsChannel = Channel<A>(UNLIMITED)

    private var _actionsJob: Job? = null
    private var _reduceJob: Job? = null

    fun start(coroutineScope: CoroutineScope) {
        stop()
        _running.set(true)
        _actionsJob = coroutineScope.observeWith {
            val intent = _intentsChannel.receive()
            val newActions = mutableListOf<A>()
            knotState.changeState { state ->
                val effect = reducer(state, intent)
                newActions.addAll(effect.actions)
                effect.state
            }
            newActions.forEach { _actionsChannel.offer(it) }
        }
        _reduceJob = coroutineScope.observeWith {
            val action = _actionsChannel.receive()
            val intent = performer?.invoke(action) ?: suspendPerformer?.invoke(action)
            intent?.run { _intentsChannel.offer(intent) }
        }
    }

    override fun offerIntent(intent: C) {
        _intentsChannel.offer(intent)
    }

    fun stop() {
        _running.set(false)
        _actionsJob?.cancel()
        _reduceJob?.cancel()
    }

    private fun CoroutineScope.observeWith(block: suspend () -> Unit) =
        launch(context = dispatcher) {
            while (_running.get()) {
                block()
            }
        }
}