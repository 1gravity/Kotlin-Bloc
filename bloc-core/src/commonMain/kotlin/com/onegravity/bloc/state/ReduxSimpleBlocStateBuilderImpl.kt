package com.onegravity.bloc.state

import com.onegravity.bloc.state.redux.ReduxBlocStateBuilderImpl
import com.onegravity.bloc.state.redux.ReduxSimpleBlocStateBuilder

class ReduxSimpleBlocStateBuilderImpl<State: Any, ReduxModel: Any> :
    ReduxSimpleBlocStateBuilder<State, ReduxModel>,
    ReduxBlocStateBuilderImpl<State, State, ReduxModel>() {

    init {
        map { it }
    }
}