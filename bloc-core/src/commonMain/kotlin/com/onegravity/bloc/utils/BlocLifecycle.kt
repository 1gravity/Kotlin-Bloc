package com.onegravity.bloc.utils

import com.onegravity.bloc.fsm.StateMachine
import com.onegravity.bloc.fsm.Transition

internal sealed class LifecycleStatus {
    object NotStarted : LifecycleStatus()
    object Started : LifecycleStatus()
    object Destroyed : LifecycleStatus()
}

internal sealed class LifecycleEvent {
    object Started : LifecycleEvent()
    object Destroyed : LifecycleEvent()
}

@Suppress("FunctionName")
internal fun BlocLifecycle(onStart: () -> Unit, onDestroy: () -> Unit) =
    StateMachine.create<LifecycleStatus, LifecycleEvent, () -> Unit> {
        initialState(LifecycleStatus.NotStarted)
        state<LifecycleStatus.NotStarted> {
            on<LifecycleEvent.Started> {
                transitionTo(LifecycleStatus.Started, onStart)
            }
        }
        state<LifecycleStatus.Started> {
            on<LifecycleEvent.Destroyed> {
                transitionTo(LifecycleStatus.Destroyed, onDestroy)
            }
        }
        state<LifecycleStatus.Destroyed> {
            // this is the final state -> no transitions
        }
        onTransition {
            val validTransition = it as? Transition.Valid ?: return@onTransition
            when (validTransition.sideEffect) {
                onStart -> logger.i("onStart -> start Bloc")
                onDestroy -> logger.i("onDestroy -> stop Bloc")
            }
            validTransition.sideEffect?.invoke()
        }
    }