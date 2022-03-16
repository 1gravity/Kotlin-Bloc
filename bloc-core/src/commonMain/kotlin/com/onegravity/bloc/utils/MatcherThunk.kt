package com.onegravity.bloc.utils

data class MatcherThunk<State, Action: Any>(
    val matcher: Matcher<Action, Action>?,
    val thunk: Thunk<State, Action>
)
