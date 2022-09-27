package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.Effect
import com.onegravity.bloc.utils.Initializer
import com.onegravity.bloc.utils.ReducerNoAction
import com.onegravity.bloc.utils.ThunkNoAction

/**
 * The BlocExtension interface defines the part of the Bloc that runs thunks, reducers and
 * initializer MVVM+ style.
 */
internal interface BlocExtension<State : Any, Action : Any, SideEffect : Any, Proposal : Any> {

    /**
     * Execute an Initializer.
     *
     * The Initializer is launched in a CoroutineScope managed by the bloc's lifecycle.
     * Only one Initializer can be executed during the lifetime of a bloc.
     */
    fun initialize(initialize: Initializer<State, Action>)

    /**
     * Dispatch a reducer to the bloc.
     *
     * The reducer is added to the dispatch queue like any other reducer
     * -> reducers are executed sequentially and in the order they are received.
     */
    fun reduce(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>)

    /**
     * Dispatch a thunk to the bloc.
     *
     * Since thunks are asynchronous by nature, more than one thunk can run at the same time.
     */
    fun thunk(thunk: ThunkNoAction<State, Action>)

}
