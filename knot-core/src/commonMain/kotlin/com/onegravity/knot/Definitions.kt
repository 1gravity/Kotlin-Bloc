package com.onegravity.knot

import kotlinx.coroutines.CoroutineScope

/** A function accepting an `Event` and a `State` and returning a `Proposal` with `SideEffects`. */
typealias Reducer<State, Event, Proposal, SideEffect> = (
    coroutineScope: CoroutineScope,
    state: State,
    event: Event
) -> Effect<Proposal, SideEffect>

/** A function used for performing given `SideEffect` and emitting resulting `Event`. */
typealias Executor<SideEffect, Event> = (coroutineScope: CoroutineScope, SideEffect) -> Event?

typealias Acceptor<State, Proposal, Model> = (state: State, proposal: Proposal) -> Model

typealias Mapper<Model, State> = (model: Model) -> State
