package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.*

/**
 * Bloc API to run thunks / reducers "externally" (using extension functions)
 */
internal interface BlocExtension<State : Any, Action : Any, SideEffect : Any, Proposal : Any> {

    /**
     * The Initializer runs synchronously
     */
    fun initialize(initialize: Initializer<State, Action>)

    /**
     * The Reducer runs synchronously
     */
    fun reduce(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>)


    /**
     * The Thunk runs asynchronously
     */
    fun thunk(thunk: ThunkNoAction<State, Action>)

}
