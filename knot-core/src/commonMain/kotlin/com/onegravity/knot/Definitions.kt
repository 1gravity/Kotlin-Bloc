package com.onegravity.knot

// todo this needs to be renamed...
internal data class SideEffect<out Intent>(val block: () -> Intent?)
internal data class SuspendSideEffect<out Intent>(val block: suspend () -> Intent?)

/** A function accepting an `Action` and a `State` and returning a `Model` with `SideEffects`. */
typealias Reducer<State, Action, Model, SideEffect> = (state: State, action: Action) -> Effect<Model, SideEffect>

/** A suspend function accepting an `Action` and a `State` and returning a `Model` with `SideEffects`. */
typealias SuspendReducer<State, Action, Model, SideEffect> = suspend (state: State, intent: Action) -> Effect<Model, SideEffect>

/** A function used for performing given `Action` and emitting resulting `Intent`. */
typealias Performer<SideEffect, Action> = (SideEffect) -> Action?

/** A suspend function used for performing given `Action` and emitting resulting `Intent`. */
typealias SuspendPerformer<SideEffect, Action> = suspend (SideEffect) -> Action?