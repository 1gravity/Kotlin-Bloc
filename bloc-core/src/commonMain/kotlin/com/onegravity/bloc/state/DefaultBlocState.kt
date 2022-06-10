package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor

/**
 * The default BlocState used in most of the Blocs.
 *
 * It takes an Acceptor function as argument to accept/reject Proposals and map them to State.
 */
internal open class DefaultBlocState<State : Any, Proposal : Any>(
    initialState: State,
    private val accept: Acceptor<Proposal, State>,
) : BlocStateBase<State, Proposal>(initialState) {

    override fun send(proposal: Proposal) {
        accept(proposal, value)?.also { state.send(it) }
    }

}
