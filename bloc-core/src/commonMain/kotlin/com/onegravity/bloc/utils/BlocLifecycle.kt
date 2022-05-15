package com.onegravity.bloc.utils

import com.onegravity.bloc.fsm.StateMachine
import com.onegravity.bloc.fsm.Transition

internal sealed class LifecycleState {
    object InitialState : LifecycleState()
    object Initialized : LifecycleState()
    object Started : LifecycleState()
    object Stopped : LifecycleState()
    object Destroyed : LifecycleState()
}

internal sealed class LifecycleEvent {
    object Initialize : LifecycleEvent()
    object Start : LifecycleEvent()
    object Stop : LifecycleEvent()
    object Destroy : LifecycleEvent()
}

typealias LifecycleCSideEffect = () -> Unit

@Suppress("FunctionName")
internal fun BlocLifecycle(
    onInitialize: LifecycleCSideEffect,
    onStart: LifecycleCSideEffect,
    onStop: LifecycleCSideEffect,
    onDestroy: LifecycleCSideEffect
) = StateMachine.create<LifecycleState, LifecycleEvent, LifecycleCSideEffect> {
    initialState(LifecycleState.InitialState)

    state<LifecycleState.InitialState> {
        on<LifecycleEvent.Initialize> {
            transitionTo(LifecycleState.Initialized, onInitialize)
        }
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Initialized) {
                onInitialize()
                onStart()
            }
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Destroyed, onDestroy)
        }
    }

    state<LifecycleState.Initialized> {
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Started, onStart)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Started, onDestroy)
        }
    }

    state<LifecycleState.Started> {
        on<LifecycleEvent.Stop> {
            transitionTo(LifecycleState.Stopped, onStop)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Started, ) {
                onStop()
                onDestroy()
            }
        }
    }

    state<LifecycleState.Stopped> {
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Started, onStart)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Destroyed, onDestroy)
        }
    }

    state<LifecycleState.Destroyed> {
        // this is the final state -> no transitions
        on<LifecycleEvent.Initialize> {
            logger.e("a destroyed Bloc can't be initialized")
            dontTransition()
        }
        on<LifecycleEvent.Start> {
            logger.e("a destroyed Bloc can't be started")
            dontTransition()
        }
        on<LifecycleEvent.Stop> {
            logger.e("a destroyed Bloc can't be stopped")
            dontTransition()
        }
        on<LifecycleEvent.Destroy> {
            logger.e("a destroyed Bloc can't be destroyed again")
            dontTransition()
        }
    }

    onTransition {
        val validTransition = it as? Transition.Valid ?: return@onTransition
        when (validTransition.sideEffect) {
            onInitialize -> logger.d("onInitialize -> initialize Bloc")
            onStart -> logger.d("onStart -> start Bloc")
            onStop -> logger.d("onStop -> stop Bloc")
            onDestroy -> logger.d("onDestroy -> destroy Bloc")
        }
        validTransition.sideEffect?.invoke()
    }
}
