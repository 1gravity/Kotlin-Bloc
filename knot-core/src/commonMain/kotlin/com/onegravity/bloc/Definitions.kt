package com.onegravity.bloc

import org.reduxkotlin.GetState

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
