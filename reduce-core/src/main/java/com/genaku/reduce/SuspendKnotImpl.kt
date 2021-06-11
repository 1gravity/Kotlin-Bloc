package com.genaku.reduce

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

class SuspendKnotImpl<S : State, C : StateIntent, A : StateAction>(
    private val knotState: CoroutineKnotState<S>,
    private val reducer: SuspendReducer<S, C, A>,
    private val performer: SuspendPerformer<A, C>,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<S, C>, JobSwitcher, KnotState<S> by knotState {

    private val _intentsChannel = Channel<C>(Channel.UNLIMITED)
    private val _actionsChannel = Channel<A>(Channel.UNLIMITED)

    private var _intentsJob: Job? = null
    private var _actionsJob: Job? = null

    override fun start(coroutineScope: CoroutineScope) {
        stop()
        _intentsJob = coroutineScope.observeWith {
            val intent = _intentsChannel.receive()
            val newActions = mutableListOf<A>()
            knotState.changeState { state ->
                val effect = reducer(state, intent)
                newActions.addAll(effect.actions)
                effect.state
            }
            newActions.forEach {
                _actionsChannel.offer(it)
            }
        }
        _actionsJob = coroutineScope.observeWith {
            val action = _actionsChannel.receive()
            val intent = performer.invoke(action)
            intent?.run { _intentsChannel.offer(intent) }
        }
    }

    override fun offerIntent(intent: C) {
        _intentsChannel.offer(intent)
    }

    override fun stop() {
        _intentsJob?.cancel()
        _actionsJob?.cancel()
    }

    private fun CoroutineScope.observeWith(block: suspend () -> Unit) =
        launch(context = dispatcher) {
            while (true) {
                ensureActive()
                block()
            }
        }
}