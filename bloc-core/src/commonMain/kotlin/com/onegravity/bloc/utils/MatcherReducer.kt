package com.onegravity.bloc.utils

internal data class MatcherReducer<State : Any, Action : Any, Proposal : Any>(
    internal val matcher: Matcher<Action, Action>?,
    internal val reducer: Reducer<State, Action, Proposal>,
    internal val expectsProposal: Boolean
)
