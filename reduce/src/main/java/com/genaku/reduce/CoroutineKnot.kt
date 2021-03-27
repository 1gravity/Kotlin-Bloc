package com.genaku.reduce

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

class CoroutineKnot<State : Any, Change : Any, Action : Any>(
    private val knotState: CoroutineKnotState<State>,
    private val reducer: Reducer<State, Change, Action>,
    private val performer: Performer<Action, Change>?,
    private val suspendPerformer: SuspendPerformer<Action, Change>?,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<State, Change>, KnotState<State> by knotState {

    private val _running = AtomicBoolean(false)

    private val _changesChannel = Channel<Change>(UNLIMITED)
    private val _actionsChannel = Channel<Action>(UNLIMITED)

    fun start(coroutineScope: CoroutineScope) {
        _running.set(true)
        coroutineScope.observeWith {
            val change = _changesChannel.receive()
            val newActions = mutableListOf<Action>()
            knotState.changeState { state ->
                val effect = reducer(state, change)
                newActions.addAll(effect.actions)
                effect.state
            }
            newActions.forEach { _actionsChannel.offer(it) }
        }
        coroutineScope.observeWith {
            val action = _actionsChannel.receive()
            val change = performer?.invoke(action) ?: suspendPerformer?.invoke(action)
            change?.run { _changesChannel.offer(change) }
        }
    }

    override fun offerChange(change: Change) {
        _changesChannel.offer(change)
    }

    fun stop() {
        _running.set(false)
    }

    private fun CoroutineScope.observeWith(block: suspend () -> Unit) =
        launch(context = dispatcher) {
            while (_running.get()) {
                block()
            }
        }
}