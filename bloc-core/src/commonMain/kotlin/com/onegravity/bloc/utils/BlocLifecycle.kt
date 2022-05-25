package com.onegravity.bloc.utils

import com.onegravity.bloc.fsm.StateMachine
import com.onegravity.bloc.fsm.Transition

internal sealed class LifecycleState {
    object InitialState : LifecycleState()
    object Created : LifecycleState()
    object Started : LifecycleState()
    object Stopped : LifecycleState()
    object Destroyed : LifecycleState()
}

internal sealed class LifecycleEvent {
    object Create : LifecycleEvent()
    object Start : LifecycleEvent()
    object Stop : LifecycleEvent()
    object Destroy : LifecycleEvent()
}

internal typealias LifecycleCSideEffect = () -> Unit

@Suppress("FunctionName")
internal fun BlocLifecycle(
    onCreate: LifecycleCSideEffect,
    onStart: LifecycleCSideEffect,
    onStop: LifecycleCSideEffect,
    onDestroy: LifecycleCSideEffect
) = StateMachine.create<LifecycleState, LifecycleEvent, LifecycleCSideEffect> {
    initialState(LifecycleState.InitialState)

    state<LifecycleState.InitialState> {
        on<LifecycleEvent.Create> {
            transitionTo(LifecycleState.Created, onCreate)
        }
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Created) {
                onCreate()
                onStart()
            }
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Destroyed, onDestroy)
        }
    }

    state<LifecycleState.Created> {
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Started, onStart)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Started, onDestroy)
        }
    }

    state<LifecycleState.Started> {
        on<LifecycleEvent.Start> {
            logger.e("a started Bloc can't be started again")
            dontTransition()
        }
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
        on<LifecycleEvent.Stop> {
            logger.e("a stopped Bloc can't be stopped again")
            dontTransition()
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Destroyed, onDestroy)
        }
    }

    state<LifecycleState.Destroyed> {
        // this is the final state -> no transitions
        on<LifecycleEvent.Create> {
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
            onCreate -> logger.d("onCreate -> initialize Bloc")
            onStart -> logger.d("onStart -> start Bloc")
            onStop -> logger.d("onStop -> stop Bloc")
            onDestroy -> logger.d("onDestroy -> destroy Bloc")
        }
        validTransition.sideEffect?.invoke()
    }
}
