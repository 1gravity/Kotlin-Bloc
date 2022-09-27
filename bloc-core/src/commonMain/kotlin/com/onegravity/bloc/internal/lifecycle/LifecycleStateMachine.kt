package com.onegravity.bloc.internal.lifecycle

import com.onegravity.bloc.internal.fsm.StateMachine
import com.onegravity.bloc.internal.fsm.Transition
import com.onegravity.bloc.utils.logger

internal data class LifecycleTransition(val from: LifecycleState, val to: LifecycleState)
internal typealias LifecycleSideEffect = List<LifecycleState>

@Suppress("FunctionName", "FunctionNaming", "RemoveExplicitTypeArguments")
internal fun LifecycleStateMachine(
    observer: (transition: LifecycleTransition) -> Unit,
) = StateMachine.create<LifecycleState, LifecycleEvent, LifecycleSideEffect> {

    initialState(LifecycleState.InitialState)

    state<LifecycleState.InitialState> {
        on<LifecycleEvent.Create> {
            transitionTo(LifecycleState.Created)
        }
    }

    state<LifecycleState.Created> {
        on<LifecycleEvent.Create> {
            logger.e("a created Bloc can't be created twice")
            dontTransition()
        }
        on<LifecycleEvent.StartInitializer> {
            transitionTo(LifecycleState.Initializing)
        }
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Starting)
        }
        on<LifecycleEvent.Stop> {
            logger.e("a Bloc must be started before it can be stopped")
            dontTransition()
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Destroyed)
        }
    }

    state<LifecycleState.Starting> {
        on<LifecycleEvent.Start> {
            logger.e("a starting Bloc can't be started twice")
            dontTransition()
        }
        on<LifecycleEvent.StartInitializer> {
            transitionTo(LifecycleState.InitializingStarting)
        }
        on<LifecycleEvent.Stop> {
            transitionTo(LifecycleState.Stopped)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(
                LifecycleState.Destroyed,
                listOf(LifecycleState.Stopped, LifecycleState.Destroyed)
            )
        }
    }

    state<LifecycleState.Initializing> {
        on<LifecycleEvent.StartInitializer> {
            logger.e("Initializer already running")
            dontTransition()
        }
        on<LifecycleEvent.InitializerCompleted> {
            transitionTo(LifecycleState.Initialized)
        }
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.InitializingStarting)
        }
        on<LifecycleEvent.Stop> {
            transitionTo(LifecycleState.Stopped)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(
                LifecycleState.Destroyed,
                listOf(LifecycleState.Stopped, LifecycleState.Destroyed)
            )
        }
    }

    state<LifecycleState.Initialized> {
        on<LifecycleEvent.InitializerCompleted> {
            logger.e("Initializer already completed")
            dontTransition()
        }
        on<LifecycleEvent.StartInitializer> {
            logger.e("Initializer already completed")
            dontTransition()
        }
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Started)
        }
        on<LifecycleEvent.Stop> {
            transitionTo(LifecycleState.Stopped)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(
                LifecycleState.Destroyed,
                listOf(LifecycleState.Stopped, LifecycleState.Destroyed)
            )
        }
    }

    state<LifecycleState.InitializingStarting> {
        on<LifecycleEvent.InitializerCompleted> {
            transitionTo(LifecycleState.Started)
        }
        on<LifecycleEvent.StartInitializer> {
            logger.e("Initializer already completed")
            dontTransition()
        }
        on<LifecycleEvent.Start> {
            logger.e("a starting Bloc can't be started twice")
            dontTransition()
        }
        on<LifecycleEvent.Stop> {
            transitionTo(LifecycleState.Stopped)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(
                LifecycleState.Destroyed,
                listOf(LifecycleState.Stopped, LifecycleState.Destroyed)
            )
        }
    }

    state<LifecycleState.Started> {
        on<LifecycleEvent.Start> {
            logger.e("a started Bloc can't be started twice")
            dontTransition()
        }
        on<LifecycleEvent.StartInitializer> {
            logger.e("Can't start initializer if the bloc is already started")
            dontTransition()
        }
        on<LifecycleEvent.Stop> {
            transitionTo(LifecycleState.Stopped)
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(
                LifecycleState.Destroyed,
                listOf(LifecycleState.Started, LifecycleState.Stopped, LifecycleState.Destroyed)
            )
        }
    }

    state<LifecycleState.Stopped> {
        on<LifecycleEvent.StartInitializer> {
            logger.e("Initializer already completed")
            dontTransition()
        }
        on<LifecycleEvent.Start> {
            transitionTo(LifecycleState.Started)
        }
        on<LifecycleEvent.Stop> {
            logger.e("a stopped Bloc can't be stopped twice")
            dontTransition()
        }
        on<LifecycleEvent.Destroy> {
            transitionTo(LifecycleState.Destroyed)
        }
    }

    state<LifecycleState.Destroyed> {
        // this is the final state -> no transitions
        on<LifecycleEvent.Create> {
            logger.e("Can't create an already destroyed Bloc")
            dontTransition()
        }
        on<LifecycleEvent.StartInitializer> {
            logger.e("Can't start initializer for an already destroyed Bloc")
            dontTransition()
        }
        on<LifecycleEvent.InitializerCompleted> {
            logger.e("Initializer completed for an already destroyed Bloc -> we should investigate for race conditions")
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
        if (validTransition.fromState == validTransition.toState) return@onTransition

        logger.d("transition from ${validTransition.fromState} to ${validTransition.toState}")

        // if the transition has side effects (a list of states), we emit all transitions one by one
        validTransition.sideEffect
            // the transition has a side effect -> emit all transitions one by one
            ?.reduceOrNull { fromState, toState ->
                observer(LifecycleTransition(fromState, toState))
                toState
            }
        // the transition has no side effect -> emit a single transition
            ?: observer(LifecycleTransition(validTransition.fromState, validTransition.toState))
    }
}
