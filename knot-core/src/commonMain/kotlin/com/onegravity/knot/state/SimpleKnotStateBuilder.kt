package com.onegravity.knot.state

import com.onegravity.bloc.BlocState

/**
 * This builder creates a [BlocState] that only stores the value produced by the Knot without any
 * logic to accept and/or map from a proposal to a model to state.
 * Whatever the Knot's reducer returns will be streamed to its consumers (the ui usually) as is.
 */
interface SimpleKnotStateBuilder<State> {

    fun build(): BlocState<State, State>

    var initialState: State

}