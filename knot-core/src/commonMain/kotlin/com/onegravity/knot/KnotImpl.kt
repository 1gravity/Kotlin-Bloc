package com.onegravity.knot

import com.onegravity.knot.state.CoroutineKnotState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class KnotImpl<out State, in Action, Model, SideEffect>(
    private val knotState: CoroutineKnotS   tate<Model>,
    private val reducer: Reducer<Model, Intent, SideEffect>,
    private val performer: Performer<SideEffect, Intent>?,
    private val suspendPerformer: SuspendPerformer<SideEffect, Intent>?,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : Knot<State, Action, Model, SideEffect>,
    JobSwitcher
{

    private val _intents = Channel<Action>(UNLIMITED)
    private val _actions = Channel<SideEffect>(UNLIMITED)

    private var _intentsJob: Job? = null
    private var _actionsJob: Job? = null

    override fun send(value: Action) {
        _intents.trySend(value)
    }

    override fun receive(receiver: StreamReceiver<State>) {
        knotState.stream.receive(receiver)
    }

    override val state: StateFlow<State>
        get() = TODO("Not yet implemented")

    override val value: Model
        get() = TODO("Not yet implemented")

//    override fun offerIntent(intent: Intent) {
//        _intents.trySend(intent)
//    }

    override fun start(coroutineScope: CoroutineScope) {
        stop()

        _intentsJob = coroutineScope.launch(dispatcher) {
            for (intent in _intents) {
                val effect = reducer(knotState.state.value, intent)
                knotState.changeState { effect.proposal }
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