package com.onegravity.bloc.utils

import com.onegravity.bloc.fsm.Matcher

data class MatcherThunk<State, Action: Any, A: Action>(
    val matcher: Matcher<Action, Action>?,
    val thunk: Thunk<State, Action, A>
)
