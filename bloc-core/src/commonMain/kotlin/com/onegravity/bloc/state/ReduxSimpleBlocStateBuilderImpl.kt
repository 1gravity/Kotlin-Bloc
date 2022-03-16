package com.onegravity.bloc.state

class ReduxSimpleBlocStateBuilderImpl<State: Any, ReduxModel: Any> :
    ReduxSimpleBlocStateBuilder<State, ReduxModel>,
    ReduxBlocStateBuilderImpl<State, State, ReduxModel>() {

    init {
        map { it }
    }
}