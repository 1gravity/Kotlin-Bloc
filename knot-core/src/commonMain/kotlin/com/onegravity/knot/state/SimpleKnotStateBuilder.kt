package com.onegravity.knot.state

/**
 * This builder creates a [KnotState] that only stores the value produced by the Knot without any
 * logic to accept and/or map from a proposal to a model to state.
 * Whatever the Knot's reducer returns will be streamed to its consumers (the ui usually) as is.
 */
interface SimpleKnotStateBuilder<State> {

    fun build(): KnotState<State, State>

    var initialState: State

}