package com.onegravity.knot

/** A function accepting an `Event` and a `State` and returning a `Proposal` with `SideEffects`. */
typealias Reducer<State, Event, Proposal, SideEffect> = (state: State, event: Event) -> Effect<Proposal, SideEffect>

/** A function used for performing given `SideEffect` and emitting resulting `Event`. */
typealias Executor<SideEffect, Event> = (SideEffect) -> Event?

typealias Acceptor<State, Proposal, Model> = (state: State, proposal: Proposal) -> Model

typealias Mapper<Model, State> = (model: Model) -> State
