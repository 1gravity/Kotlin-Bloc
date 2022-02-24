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

    private val _intents = Channel<C>(UNLIMITED)
    private val _actions = Channel<A>(UNLIMITED)

    private var _intentsJob: Job? = null
    private var _actionsJob: Job? = null

    override fun offerIntent(intent: C) {
        _intents.trySend(intent)
    }

    override fun start(coroutineScope: CoroutineScope) {
        stop()

        _intentsJob = coroutineScope.launch(dispatcher) {
            for (intent in _intents) {
                val effect = reducer(knotState.state.value, intent)
                knotState.changeState { effect.state }
                effect.actions.forEach { _actions.send(it) }
            }
        }

        _actionsJob = coroutineScope.launch(dispatcher) {
            for (action in _actions) {
                val intent = performer?.invoke(action) ?: suspendPerformer?.invoke(action)
                intent?.run { _intents.send(intent) }
            }
        }
    }

    override fun stop() {
        _intentsJob?.cancel("")?.also { _intentsJob = null }
        _actionsJob?.cancel("")?.also { _actionsJob = null }
    }

}