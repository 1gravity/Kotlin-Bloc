package com.onegravity.knot

/** A function accepting an `Event` and a `State` and returning a `Proposal` with `SideEffects`. */
typealias Reducer<State, Event, Proposal, SideEffect> = suspend (state: State, event: Event) -> Effect<Proposal, SideEffect>

/** A function used for performing given `SideEffect` and emitting resulting `Event`. */
typealias Executor<SideEffect, Event> = suspend (SideEffect) -> Event?

typealias Acceptor<State, Proposal, Model> = (state: State, proposal: Proposal) -> Model

typealias Mapper<Model, State> = (model: Model) -> State
