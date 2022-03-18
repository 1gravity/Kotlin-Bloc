package com.onegravity.bloc.utils

data class MatcherSideEffect<State, Action: Any, SIDE_EFFECT>(
    val matcher: Matcher<Action, Action>?,
    val sideEffect: SideEffect<State, Action, SIDE_EFFECT>
)
