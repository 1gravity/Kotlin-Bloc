package com.onegravity.knot.state

import com.onegravity.knot.Selector

/**
 * Builder for a [ReduxKnotState].
 * Unlike [ReduxKnotStateBuilder] this builder assumes the map function is { it } (no mapping).
 */
interface ReduxSimpleKnotStateBuilder<State, ReduxModel> {

    var initialState: State

    fun select(selector: Selector<ReduxModel, State>)

}