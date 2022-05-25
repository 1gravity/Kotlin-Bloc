package com.onegravity.bloc.state

import com.onegravity.bloc.utils.BlocDSL

/**
 * This builder creates a [BlocState] that only stores the value produced by the Bloc without any
 * logic to accept the proposal and map it to a state.
 * Whatever the Bloc's reducer returns will be streamed to its consumers (the ui usually) as is.
 */
interface SimpleBlocStateBuilder<State : Any> {

    @BlocDSL
    fun build(): BlocState<State, State>

    @BlocDSL
    var initialState: State

}