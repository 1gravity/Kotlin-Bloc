package com.genaku.reduce

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mym.plog.PLog
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

class CoroutineKnot<State : Any, Change : Any, Action : Any>(
    initialState: State,
    private val reducer: Reducer<State, Change, Action>,
    private val performer: Performer<Action, Change>?,
    private val suspendPerformer: SuspendPerformer<Action, Change>?,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<State, Change> {

    private val _running = AtomicBoolean(false)

    private val _changesChannel = Channel<Change>(UNLIMITED)
    private val _actionsChannel = Channel<Action>(UNLIMITED)

    private val _state = MutableStateFlow(initialState)

    override val state: StateFlow<State> = _state

    fun start(coroutineScope: CoroutineScope) {
        _running.set(true)
        coroutineScope.observeWith {
            val change = _changesChannel.receive()
            PLog.d("change $change")
            val effect = reducer(_state.value, change)
            effect.emitActions(_actionsChannel)
            _state.emit(effect.state)
        }
        coroutineScope.observeWith {
            val action = _actionsChannel.receive()
            PLog.d("action $action")
            val change = performer?.invoke(action) ?: suspendPerformer?.invoke(action)
            change?.run { _changesChannel.offer(change) }
        }
    }

    override fun offerChange(change: Change) {
        PLog.d("emit $change")
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

internal fun <State : Any, Action : Any> Effect<State, Action>.emitActions(channel: Channel<Action>) {
    actions.forEach { channel.offer(it) }
}