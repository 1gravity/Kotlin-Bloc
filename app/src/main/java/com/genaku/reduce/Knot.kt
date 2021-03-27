package com.genaku.reduce

import kotlinx.coroutines.flow.StateFlow

interface Knot<State : Any, Change : Any> {
    val state: StateFlow<State>
    fun offerChange(change: Change)
}

/** A function accepting the `State` and a `Change` and returning a new `State` with `Actions`. */
typealias Reducer<State, Change, Action> = State.(change: Change) -> Effect<State, Action>

/** A function used for performing given `Action` and emitting resulting `Change`. */
typealias Performer<Action, Change> = (Action) -> Change?

/** A function used for performing given `Action` and emitting resulting `Change`. */
typealias SuspendPerformer<Action, Change> = suspend (Action) -> Change?

/** Convenience wrapper around [State] and optional [Action]s. */
data class Effect<State : Any, Action : Any>(
    val state: State,
    val actions: List<Action> = emptyList()
) {
    operator fun plus(action: Action): Effect<State, Action> = Effect(state, actions + action)
}
