package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.Effect
import com.onegravity.bloc.utils.ReducerNoAction

/**
 * Wrapper class for reducers that are submitted Redux style (send(Action)) or MVVM+ style (reduce { })
 */
internal data class ReduceChannelElement<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    val action: Action? = null,
    val reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>? = null
)
