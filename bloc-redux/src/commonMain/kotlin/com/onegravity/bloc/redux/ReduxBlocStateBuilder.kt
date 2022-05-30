package com.onegravity.bloc.redux

import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector

/**
 * Builder for a [ReduxBlocState].
 */
internal interface ReduxBlocStateBuilder<State : Any, Model : Any, ReduxModel : Any> {

    @BlocDSL
    fun select(selector: Selector<ReduxModel, Model>)

    @BlocDSL
    fun map(mapper: Mapper<Model, State>)

}