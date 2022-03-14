package com.onegravity.knot

/** A function accepting an `Event` and a `State` and returning a `Proposal` with `SideEffects`. */
typealias Reducer<State, Event, Proposal, SideEffect> = suspend (state: State, event: Event) -> Effect<Proposal, SideEffect>

/** A function used for performing given `SideEffect` and emitting resulting `Event`. */
typealias Executor<SideEffect, Event> = suspend (SideEffect) -> Event?

/** A function used for accepting or rejecting a [Proposal] to updating and emitting resulting [State]. */
typealias Acceptor<Proposal, State> = (proposal: Proposal, state: State) -> State

typealias Mapper<Model, State> = (model: Model) -> State

typealias Selector<State, Model> = (State) -> Model
