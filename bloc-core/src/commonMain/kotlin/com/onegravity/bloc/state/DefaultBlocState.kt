package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor

internal open class DefaultBlocState<State : Any, Proposal : Any>(
    initialState: State,
    private val acceptor: Acceptor<Proposal, State>,
) : BlocStateBase<State, Proposal>(initialState) {

    override fun send(proposal: Proposal) {
        acceptor(proposal, value)?.also { state.send(it) }
    }

}
