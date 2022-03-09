package com.onegravity.knot.state

import com.onegravity.knot.Acceptor
import com.onegravity.knot.Mapper

/**
 * This builder creates a [KnotState] that uses the [accept] function to process the Knot's proposal
 * and potentially updates the [State].
 */
interface KnotStateBuilder<State, Proposal> {

    fun build(): KnotState<State, Proposal>

    var initialState: State

    fun accept(acceptor: Acceptor<Proposal, State>)

}