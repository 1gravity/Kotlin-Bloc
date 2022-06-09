package com.onegravity.bloc.internal.builder

import com.onegravity.bloc.utils.Thunk

internal data class MatcherThunk<State : Any, Action : Any, A : Action>(
    internal val matcher: Matcher<Action, Action>?,
    internal val thunk: Thunk<State, Action, A>
)
