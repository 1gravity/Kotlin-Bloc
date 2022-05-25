package com.onegravity.bloc.redux

import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector

/**
 * Builder for a [ReduxBlocState].
 */
interface ReduxBlocStateBuilder<State : Any, Model : Any, ReduxModel : Any> {

    @BlocDSL
    var initialState: State

    @BlocDSL
    fun select(selector: Selector<ReduxModel, Model>)

    @BlocDSL
    fun map(mapper: Mapper<Model, State>)

}