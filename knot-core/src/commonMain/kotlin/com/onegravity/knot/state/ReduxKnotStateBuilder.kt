package com.onegravity.knot.state

import com.onegravity.knot.Mapper
import com.onegravity.knot.Selector

/**
 * Builder for a [ReduxKnotState].
 */
interface ReduxKnotStateBuilder<State, Model, ReduxModel> {

    var initialState: State

    fun select(selector: Selector<ReduxModel, Model>)

    fun map(mapper: Mapper<Model, State>)

}