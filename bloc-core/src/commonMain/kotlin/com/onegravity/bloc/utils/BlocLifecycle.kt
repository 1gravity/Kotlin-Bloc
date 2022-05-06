package com.onegravity.bloc.utils

import com.onegravity.bloc.fsm.StateMachine
import com.onegravity.bloc.fsm.Transition

internal sealed class LifecycleStatus {
    object NotStarted : LifecycleStatus()
    object Started : LifecycleStatus()
    object Stopped : LifecycleStatus()
}

internal sealed class LifecycleEvent {
    object Start : LifecycleEvent()
    object Stop : LifecycleEvent()
}

@Suppress("FunctionName")
internal fun BlocLifecycle(onStart: () -> Unit, onStop: () -> Unit) =
    StateMachine.create<LifecycleStatus, LifecycleEvent, () -> Unit> {
        initialState(LifecycleStatus.NotStarted)
        state<LifecycleStatus.NotStarted> {
            on<LifecycleEvent.Start> {
                transitionTo(LifecycleStatus.Started, onStart)
            }
        }
        state<LifecycleStatus.Started> {
            on<LifecycleEvent.Stop> {
                transitionTo(LifecycleStatus.Stopped, onStop)
            }
        }
        state<LifecycleStatus.Stopped> {
            // this is the final state -> no transitions
//            on<LifecycleEvent.Start> {}
//            on<LifecycleEvent.Stop> {}
        }
        onTransition {
            val validTransition = it as? Transition.Valid ?: return@onTransition
            when (validTransition.sideEffect) {
                onStart -> logger.i("onStart -> start Bloc")
                onStop -> logger.i("onStop -> stop Bloc")
            }
            validTransition.sideEffect?.invoke()
        }
    }