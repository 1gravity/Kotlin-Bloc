package com.onegravity.bloc.builder

import com.onegravity.bloc.Thunk

data class MatcherThunk<State, Action: Any>(
    val matcher: Matcher<Action, Action>?,
    val thunk: Thunk<State, Action>
)
