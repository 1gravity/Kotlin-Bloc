package com.onegravity.knot

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlin.coroutines.CoroutineContext

class KnotImpl<S : State, Intent, SideEffect>(
    private val knotState: CoroutineKnotState<S>,
    private val reducer: Reducer<S, Intent, SideEffect>,
    private val performer: Performer<SideEffect, Intent>?,
    private val suspendPerformer: SuspendPerformer<SideEffect, Intent>?,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<S, Intent>, JobSwitcher, KnotState<S> by knotState {

    private val _intents = Channel<Intent>(UNLIMITED)
    private val _actions = Channel<SideEffect>(UNLIMITED)

    private var _intentsJob: Job? = null
    private var _actionsJob: Job? = null

    override fun offerIntent(intent: Intent) {
        _intents.trySend(intent)
    }

    override fun start(coroutineScope: CoroutineScope) {
        stop()

        _intentsJob = coroutineScope.launch(dispatcher) {
            for (intent in _intents) {
                val effect = reducer(knotState.state.value, intent)
                knotState.changeState { effect.state }
                effect.sideEffects.forEach { _actions.send(it) }
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