package com.onegravity.bloc.utils

import com.onegravity.bloc.fsm.Matcher

internal data class MatcherThunk<State : Any, Action : Any, A : Action>(
    internal val matcher: Matcher<Action, Action>?,
    internal val thunk: Thunk<State, Action, A>
)
