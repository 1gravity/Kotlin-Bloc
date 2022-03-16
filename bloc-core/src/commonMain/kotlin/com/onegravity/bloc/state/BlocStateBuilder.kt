package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor

/**
 * This builder creates a [BlocState] that uses the [accept] function to process the Bloc's proposal
 * and potentially updates the [State].
 */
interface BlocStateBuilder<State, Proposal> {

    var initialState: State

    fun accept(acceptor: Acceptor<Proposal, State>)

}