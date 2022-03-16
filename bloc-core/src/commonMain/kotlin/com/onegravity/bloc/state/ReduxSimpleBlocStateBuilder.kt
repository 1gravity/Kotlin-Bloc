package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Selector

/**
 * Builder for a [ReduxBlocState].
 * Unlike [ReduxBlocStateBuilder] this builder assumes the map function is { it } (no mapping).
 */
interface ReduxSimpleBlocStateBuilder<State, ReduxModel> {

    var initialState: State

    fun select(selector: Selector<ReduxModel, State>)

}