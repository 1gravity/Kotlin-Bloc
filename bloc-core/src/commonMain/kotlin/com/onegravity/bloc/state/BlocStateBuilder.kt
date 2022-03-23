package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor
import com.onegravity.bloc.utils.BlocDSL

/**
 * This builder creates a [BlocState] that uses the [accept] function to process the Bloc's proposal
 * and potentially updates the [State].
 */
interface BlocStateBuilder<State, Proposal> {

    @BlocDSL
    var initialState: State

    @BlocDSL
    fun accept(acceptor: Acceptor<Proposal, State>)

}