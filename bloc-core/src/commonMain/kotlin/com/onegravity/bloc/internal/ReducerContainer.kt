package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.Effect
import com.onegravity.bloc.utils.ReducerNoAction
import kotlin.coroutines.Continuation

/**
 * Wrapper class for reducers that are submitted Redux style (send(Action)) or MVVM+ style (reduce { })
 *
 * @param action Action if the reducer was triggered by an Action
 * @param reducer Reducer function if the reducer was triggered MVVM+ style
 * @param continuation Continuation if the caller is suspending till the reducer is done
 */
internal data class ReducerContainer<State : Any, Action : Any, SideEffect : Any, Proposal : Any>(
    val action: Action? = null,
    val reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>? = null,
    val continuation: Continuation<Unit>?
)
