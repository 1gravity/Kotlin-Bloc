package com.onegravity.knot

import kotlinx.coroutines.flow.StateFlow

interface State

// todo this needs to be renamed...
data class SideEffect<out Intent>(val block: () -> Intent?)
data class SuspendSideEffect<out Intent>(val block: suspend () -> Intent?)

interface Knot<S : State, in Intent> : KnotState<S> {
    fun offerIntent(intent: Intent)
}

/**
 * Knot state
 */
interface KnotState<S : State> {
    val state: StateFlow<S>
}

/** A function accepting the `State` and a `Intent` and returning a new `State` with `Actions`. */
typealias Reducer<State, Intent, Action> = (state: State, intent: Intent) -> Effect<State, Action>

/** A suspend function accepting the `State` and a `Intent` and returning a new `State` with `Actions`. */
typealias SuspendReducer<State, Intent, Action> = suspend (state: State, intent: Intent) -> Effect<State, Action>

/** A function used for performing given `Action` and emitting resulting `Intent`. */
typealias Performer<Action, Intent> = (Action) -> Intent?

/** A suspend function used for performing given `Action` and emitting resulting `Intent`. */
typealias SuspendPerformer<Action, Intent> = suspend (Action) -> Intent?

/** Convenience wrapper around [State] and optional [StateAction]s. */
data class Effect<S : State, SideEffect>(
    val state: S,
    val sideEffects: List<SideEffect> = emptyList()
) {
    operator fun plus(proposal: SideEffect): Effect<S, SideEffect> = Effect(state, sideEffects + proposal)
}
