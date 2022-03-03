package com.onegravity.knot

// todo this needs to be renamed...
internal data class SideEffect<out Intent>(val block: () -> Intent?)
internal data class SuspendSideEffect<out Intent>(val block: suspend () -> Intent?)

/**
 * A function accepting the `Intent` and a `State` and returning a `Model` with `SideEffects`.
 */
typealias Reducer<State, Intent, Model, SideEffect> = (state: State, intent: Intent) -> Effect<Model, SideEffect>

/** A suspend function accepting the `State` and a `Intent` and returning a new `State` with `Actions`. */
typealias SuspendReducer<State, Intent, Action> = suspend (state: State, intent: Intent) -> Effect<State, Action>

/** A function used for performing given `Action` and emitting resulting `Intent`. */
typealias Performer<Action, Intent> = (Action) -> Intent?

/** A suspend function used for performing given `Action` and emitting resulting `Intent`. */
typealias SuspendPerformer<Action, Intent> = suspend (Action) -> Intent?