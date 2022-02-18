package com.onegravity.knot

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlin.coroutines.CoroutineContext

class KnotImpl<S : State, C : StateIntent, A : StateAction>(
    private val knotState: CoroutineKnotState<S>,
    private val reducer: Reducer<S, C, A>,
    private val performer: Performer<A, C>?,
    private val suspendPerformer: SuspendPerformer<A, C>?,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<S, C>, JobSwitcher, KnotState<S> by knotState {

    private val _intentsChannel = Channel<C>(UNLIMITED)
    private val _actionsChannel = Channel<A>(UNLIMITED)

    private var _intentsJob: Job? = null
    private var _actionsJob: Job? = null

    private var _coroutineScope: CoroutineScope? = null

    override fun start(coroutineScope: CoroutineScope) {
        stop()
        _coroutineScope = coroutineScope
        _intentsJob = coroutineScope.observeWith {
            val intent = _intentsChannel.receive()
            val newActions = mutableListOf<A>()
            knotState.changeState { state ->
                val effect = reducer(state, intent)
                newActions.addAll(effect.actions)
                effect.state
            }
            newActions.forEach { _actionsChannel.send(it) }
        }
        _actionsJob = coroutineScope.observeWith {
            val action = _actionsChannel.receive()
            val intent = performer?.invoke(action) ?: suspendPerformer?.invoke(action)
            intent?.run { _intentsChannel.send(intent) }
        }
    }

    override fun offerIntent(intent: C) {
        _coroutineScope?.launch {
            _intentsChannel.send(intent)
        }
    }

    override fun stop() {
        _intentsJob?.cancel("")
        _intentsJob = null
        _actionsJob?.cancel("")
        _actionsJob = null
    }

    private fun CoroutineScope.observeWith(block: suspend () -> Unit) =
        launch(context = dispatcher) {
            while (true) {
                ensureActive()
                block()
            }
        }
}