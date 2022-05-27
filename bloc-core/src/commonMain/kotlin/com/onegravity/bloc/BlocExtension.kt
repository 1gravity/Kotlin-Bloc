package com.onegravity.bloc

import com.onegravity.bloc.utils.*
import kotlinx.coroutines.Job

/**
 * Bloc API to run thunks / reducers "externally" (using extension functions)
 */
public interface BlocExtension<State : Any, Action : Any, SideEffect : Any, Proposal : Any> {

    /**
     * The Initializer runs synchronously
     */
    public fun runInitializer(initialize: Initializer<State, Action>): Job?

    /**
     * The Reducer runs synchronously
     */
    public fun runReducer(reduce: ReducerNoAction<State, Effect<Proposal, SideEffect>>): Job?


    /**
     * The Thunk runs asynchronously
     */
    public fun runThunk(thunk: ThunkNoAction<State, Action>)

}
