package com.onegravity.bloc.state.redux

import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Selector

/**
 * Builder for a [ReduxBlocState].
 * Unlike [ReduxBlocStateBuilder] this builder assumes the map function is { it } (no mapping).
 */
interface ReduxSimpleBlocStateBuilder<State, ReduxModel> {

    @BlocDSL
    var initialState: State

    @BlocDSL
    fun select(selector: Selector<ReduxModel, State>)

}