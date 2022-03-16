package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Selector
import com.onegravity.bloc.utils.Mapper

/**
 * Builder for a [ReduxBlocState].
 */
interface ReduxBlocStateBuilder<State, Model, ReduxModel> {

    var initialState: State

    fun select(selector: Selector<ReduxModel, Model>)

    fun map(mapper: Mapper<Model, State>)

}