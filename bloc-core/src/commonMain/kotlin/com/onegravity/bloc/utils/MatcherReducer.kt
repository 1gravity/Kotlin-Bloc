package com.onegravity.bloc.utils

data class MatcherReducer<State, Action: Any, Proposal>(
    val matcher: Matcher<Action, Action>?,
    val reducer: Reducer<State, Action, Proposal>,
    val expectsProposal: Boolean
)
