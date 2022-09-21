package com.onegravity.bloc.internal.builder

import com.onegravity.bloc.internal.fsm.Matcher
import com.onegravity.bloc.utils.Reducer

internal data class MatcherReducer<State : Any, Action : Any, Proposal : Any>(
    internal val matcher: Matcher<Action, Action>?,
    internal val reducer: Reducer<State, Action, Proposal>,
    internal val expectsProposal: Boolean
)
