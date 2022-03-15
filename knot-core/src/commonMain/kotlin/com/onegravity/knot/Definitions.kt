package com.onegravity.knot

/** A function used for accepting or rejecting a [Proposal] to updating and emitting resulting [State]. */
typealias Acceptor<Proposal, State> = (proposal: Proposal, state: State) -> State

typealias Mapper<Model, State> = (model: Model) -> State

typealias Selector<State, Model> = (State) -> Model
