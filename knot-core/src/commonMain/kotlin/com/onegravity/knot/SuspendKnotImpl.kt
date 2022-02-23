package com.onegravity.knot

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

class SuspendKnotImpl<S : State, C : StateIntent, A : StateAction>(
    private val knotState: CoroutineKnotState<S>,
    private val reducer: SuspendReducer<S, C, A>,
    private val performer: SuspendPerformer<A, C>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<S, C>, JobSwitcher, KnotState<S> by knotState {

    private val _intents = Channel<C>(Channel.UNLIMITED)
    private val _actions = Channel<A>(Channel.UNLIMITED)

    private var _intentsJob: Job? = null
    private var _actionsJob: Job? = null

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
                val intent = performer.invoke(action)
                intent?.run { _intents.send(intent) }
            }
        }
    }

    override fun offerIntent(intent: C) {
        _intents.trySend(intent)
    }

    override fun stop() {
        _intentsJob?.cancel("")
        _intentsJob = null
        _actionsJob?.cancel("")
        _actionsJob = null
    }

}