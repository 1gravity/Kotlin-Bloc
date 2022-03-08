package com.onegravity.knot.state

import com.onegravity.knot.Acceptor
import com.onegravity.knot.Mapper

interface KnotStateBuilder<State, Proposal, Model> {

    fun build(): KnotState<State, Proposal>

    var initialState: State

    fun accept(acceptor: Acceptor<State, Proposal, Model>)

    fun map(mapper: Mapper<Model, State>)

}