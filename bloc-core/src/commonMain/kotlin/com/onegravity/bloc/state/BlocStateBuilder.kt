package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor
import com.onegravity.bloc.utils.BlocDSL

/**
 * This builder creates a [BlocState] that uses the [accept] function to process the Bloc's proposal
 * and potentially updates the [State].
 */
public interface BlocStateBuilder<State : Any, Proposal : Any> {

    @BlocDSL
    public var initialState: State

    @BlocDSL
    public fun accept(acceptor: Acceptor<Proposal, State>)

}