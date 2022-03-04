package com.onegravity.knot

// todo this needs to be renamed...
internal data class SideEffect<out Intent>(val block: () -> Intent?)
internal data class SuspendSideEffect<out Intent>(val block: suspend () -> Intent?)

/** A function accepting an `Event` and a `State` and returning a `Proposal` with `SideEffects`. */
typealias Reducer<State, Event, Proposal, SideEffect> = (state: State, event: Event) -> Effect<Proposal, SideEffect>

/** A suspend function accepting an `Event` and a `State` and returning a `Proposal` with `SideEffects`. */
typealias SuspendReducer<State, Event, Proposal, SideEffect> = suspend (state: State, event: Event) -> Effect<Proposal, SideEffect>

/** A function used for performing given `SideEffect` and emitting resulting `Event`. */
typealias Executor<SideEffect, Event> = (SideEffect) -> Event?

/** A suspend function used for performing given `SideEffect` and emitting resulting `Event`. */
typealias SuspendExecutor<SideEffect, Event> = suspend (SideEffect) -> Event?

typealias Acceptor<State, Proposal, Model> = (state: State, proposal: Proposal) -> Model

typealias Mapper<Model, State> = (model: Model) -> State
