package com.onegravity.bloc.redux

import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Selector

/**
 * Builder for a [ReduxBlocState].
 * Unlike [ReduxBlocStateBuilder] this builder assumes the map function is { it } (no mapping).
 */
internal interface ReduxSimpleBlocStateBuilder<State : Any, ReduxModel : Any> {

    @BlocDSL
    var initialState: State

    @BlocDSL
    fun select(selector: Selector<ReduxModel, State>)

}