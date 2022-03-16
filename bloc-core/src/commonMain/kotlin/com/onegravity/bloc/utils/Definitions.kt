package com.onegravity.bloc.utils

import org.reduxkotlin.GetState

/**
 * A function for accepting / rejecting a [Proposal] and updating and emitting resulting [State].
 */
typealias Acceptor<Proposal, State> = (proposal: Proposal, state: State) -> State

typealias Mapper<Model, State> = (model: Model) -> State

typealias Selector<State, Model> = (State) -> Model

typealias Dispatcher<Action> = suspend (Action) -> Unit

typealias Thunk<State, Action> = suspend (
    getState: GetState<State>,
    action: Action,
    dispatch: Dispatcher<Action>
) -> Unit

typealias Reducer<State, Action, Proposal> = (
    state: State,
    action: Action,
) -> Proposal
