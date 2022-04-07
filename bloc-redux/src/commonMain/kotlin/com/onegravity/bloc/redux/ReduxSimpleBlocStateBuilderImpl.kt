package com.onegravity.bloc.redux

internal class ReduxSimpleBlocStateBuilderImpl<State: Any, ReduxModel: Any> :
    ReduxSimpleBlocStateBuilder<State, ReduxModel>,
    ReduxBlocStateBuilderImpl<State, State, ReduxModel>() {

    init {
        // Proposal == State -> the map function is an identity function
        map { it }
    }
}