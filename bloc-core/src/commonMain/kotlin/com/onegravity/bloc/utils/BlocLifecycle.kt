package com.onegravity.bloc.utils

import com.onegravity.bloc.fsm.StateMachine
import com.onegravity.bloc.fsm.Transition

sealed class BlocState {
    object NotStarted : BlocState()
    object Started : BlocState()
    object Destroyed : BlocState()
}

sealed class BlocStateEvent {
    object Started : BlocStateEvent()
    object Destroyed : BlocStateEvent()
}

@Suppress("FunctionName")
fun BlocLifecycle(onStart: () -> Unit, onDestroy: () -> Unit) =
    StateMachine.create<BlocState, BlocStateEvent, () -> Unit> {
        initialState(BlocState.NotStarted)
        state<BlocState.NotStarted> {
            on<BlocStateEvent.Started> {
                transitionTo(BlocState.Started, onStart)
            }
        }
        state<BlocState.Started> {
            on<BlocStateEvent.Destroyed> {
                transitionTo(BlocState.Destroyed, onDestroy)
            }
        }
        state<BlocState.Destroyed> {
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