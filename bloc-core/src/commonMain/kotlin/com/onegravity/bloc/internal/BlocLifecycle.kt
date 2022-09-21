package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.internal.fsm.StateMachine.Companion.create
import com.onegravity.bloc.internal.fsm.Transition
import com.onegravity.bloc.utils.logger

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

/**
 * Creates a BlocLifecycle and ties it to an Essenty Lifecycle which is the "external" lifecycle
 * a Bloc follows. The BlocLifecycle is implemented using a finite state machine under the hood.
 */
internal fun Lifecycle.toBlocLifecycle(
    onCreate: LifecycleCSideEffect,
    onStart: LifecycleCSideEffect,
    onStop: LifecycleCSideEffect,
    onDestroy: LifecycleCSideEffect
) {
    val blocLifecycle = BlocLifecycle(onCreate, onStart, onStop, onDestroy)
    doOnCreate { blocLifecycle.transition(LifecycleEvent.Create) }
    doOnStart { blocLifecycle.transition(LifecycleEvent.Start) }
    doOnStop { blocLifecycle.transition(LifecycleEvent.Stop) }
    doOnDestroy { blocLifecycle.transition(LifecycleEvent.Destroy) }
}

@Suppress("FunctionName")
private fun BlocLifecycle(
    onCreate: LifecycleCSideEffect,
    onStart: LifecycleCSideEffect,
    onStop: LifecycleCSideEffect,
    onDestroy: LifecycleCSideEffect
) = create<LifecycleState, LifecycleEvent, LifecycleCSideEffect> {
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
        on<LifecycleEvent.Stop> {
            logger.e("a Bloc must be started before it can be stopped")
            dontTransition()
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Destroyed, onDestroy)
        }
    }

    state<LifecycleState.Created> {
        on<LifecycleEvent.Create> {
            logger.e("a created Bloc can't be created twice")
            dontTransition()
        }
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Started, onStart)
        }
        on<LifecycleEvent.Stop> {
            logger.e("a Bloc must be started before it can be stopped")
            dontTransition()
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Started, onDestroy)
        }
    }

    state<LifecycleState.Started> {
        on<LifecycleEvent.Create> {
            logger.e("Can't create an already started Bloc")
            dontTransition()
        }
        on<LifecycleEvent.Start> {
            logger.e("a started Bloc can't be started twice")
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
        on<LifecycleEvent.Create> {
            logger.e("Can't create an already stopped Bloc")
            dontTransition()
        }
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Started, onStart)
        }
        on<LifecycleEvent.Stop> {
            logger.e("a stopped Bloc can't be stopped twice")
            dontTransition()
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Destroyed, onDestroy)
        }
    }

    state<LifecycleState.Destroyed> {
        // this is the final state -> no transitions
        on<LifecycleEvent.Create> {
            logger.e("Can't create an already destroyed Bloc")
            dontTransition()
        }
        on<LifecycleEvent.Start> {
            logger.e("Can't start an already destroyed Bloc")
            dontTransition()
        }
        on<LifecycleEvent.Stop> {
            logger.e("Can't stop an already destroyed Bloc")
            dontTransition()
        }
        on<LifecycleEvent.Destroy> {
            logger.e("Can't destroy a Bloc twice")
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
