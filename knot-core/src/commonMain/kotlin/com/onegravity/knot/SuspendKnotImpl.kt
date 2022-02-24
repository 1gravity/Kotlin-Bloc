package com.onegravity.knot

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

class SuspendKnotImpl<S : State, Intent, SideEffect>(
    private val knotState: CoroutineKnotState<S>,
    private val reducer: SuspendReducer<S, Intent, SideEffect>,
    private val performer: SuspendPerformer<SideEffect, Intent>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<S, Intent>, JobSwitcher, KnotState<S> by knotState {

    private val _intents = Channel<Intent>(Channel.UNLIMITED)
    private val _actions = Channel<SideEffect>(Channel.UNLIMITED)

    private var _intentsJob: Job? = null
    private var _actionsJob: Job? = null

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
                val intent = performer.invoke(action)
                intent?.run { _intents.send(intent) }
            }
        }
    }

    override fun offerIntent(intent: Intent) {
        _intents.trySend(intent)
    }

    override fun stop() {
        _intentsJob?.cancel("")
        _intentsJob = null
        _actionsJob?.cancel("")
        _actionsJob = null
    }

}