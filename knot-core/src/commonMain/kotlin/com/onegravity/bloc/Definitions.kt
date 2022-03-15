package com.onegravity.bloc

typealias Dispatcher<Action> = suspend (Action) -> Unit

typealias Thunk<State, Action> = suspend (
    state: State,
    action: Action,
    dispatch: Dispatcher<Action>
) -> Unit

typealias ActionThunk<State, Action> = suspend (
    state: State,
    dispatch: Dispatcher<Action>
) -> Unit

data class ThunkDispatcher<State, Action>(
    val thunk: Thunk<State, Action>,
    val dispatcher: Dispatcher<Action>
)

typealias Reducer<State, Action, Proposal> = (
    state: State,
    action: Action,
) -> Proposal

