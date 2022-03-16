package com.onegravity.bloc.state

/**
 * This builder creates a [BlocState] that only stores the value produced by the Bloc without any
 * logic to accept and/or map from a proposal to a model to state.
 * Whatever the Bloc's reducer returns will be streamed to its consumers (the ui usually) as is.
 */
interface SimpleBlocStateBuilder<State> {

    fun build(): BlocState<State, State>

    var initialState: State

}