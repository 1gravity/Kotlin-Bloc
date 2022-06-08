package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.Effect
import com.onegravity.bloc.utils.ReducerNoAction

internal data class ReduceChannelElement<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    val action: Action? = null,
    val reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>? = null
)
