package com.onegravity.knot.state

/**
 * This KnotState only stores the value produced by the Knot without any logic to accept and/or
 * map from a proposal to a model to state. Whatever the Knot's reducer returns will be streamed
 * to its consumers (the ui usually) as is.
 */
class SimpleKnotState<Model>(initialState: Model) :
    KnotStateImpl<Model, Model, Model>(
        initialState, { state, _ -> state }, { it }
    )
