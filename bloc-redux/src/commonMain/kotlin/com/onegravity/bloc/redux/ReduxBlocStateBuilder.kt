package com.onegravity.bloc.redux

import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Selector
import com.onegravity.bloc.utils.Mapper

/**
 * Builder for a [ReduxBlocState].
 */
interface ReduxBlocStateBuilder<State, Model, ReduxModel> {

    @BlocDSL
    var initialState: State

    @BlocDSL
    fun select(selector: Selector<ReduxModel, Model>)

    @BlocDSL
    fun map(mapper: Mapper<Model, State>)

}