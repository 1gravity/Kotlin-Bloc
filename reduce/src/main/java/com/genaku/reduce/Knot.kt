package com.genaku.reduce

import kotlinx.coroutines.flow.StateFlow

interface State
interface Intent
interface Action
class SideEffect<I: Intent>(val block: () -> I?): Action
class SuspendSideEffect<I: Intent>(val block: suspend () -> I?): Action

interface Knot<S : State, C : Intent> : KnotState<S> {
    fun offerIntent(intent: C)
}

/** **/
interface KnotState<S : State> {
    val state: StateFlow<S>
}

/** A function accepting the `State` and a `Intent` and returning a new `State` with `Actions`. */
typealias Reducer<State, Intent, Action> = State.(intent: Intent) -> Effect<State, Action>

/** A function used for performing given `Action` and emitting resulting `Intent`. */
typealias Performer<Action, Intent> = (Action) -> Intent?

/** A function used for performing given `Action` and emitting resulting `Intent`. */
typealias SuspendPerformer<Action, Intent> = suspend (Action) -> Intent?

/** Convenience wrapper around [State] and optional [Action]s. */
data class Effect<S : State, A : Action>(
    val state: S,
    val actions: List<A> = emptyList()
) {
    operator fun plus(action: A): Effect<S, A> = Effect(state, actions + action)
}
