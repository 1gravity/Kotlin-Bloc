package com.onegravity.knot.state

class ReduxSimpleKnotStateBuilderImpl<State: Any, ReduxModel: Any> :
    ReduxSimpleKnotStateBuilder<State, ReduxModel>,
    ReduxKnotStateBuilderImpl<State, State, ReduxModel>() {

    init {
        map { it }
    }
}