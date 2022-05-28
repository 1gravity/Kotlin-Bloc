package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor

// todo the acceptor isn't accepting anything, it's just mapping so it should be called differently
//      or have a signature that would allow it to actually reject a change
internal open class DefaultBlocState<State : Any, Proposal : Any>(
    initialState: State,
    private val acceptor: Acceptor<Proposal, State>,
) : BlocStateBase<State, Proposal>(initialState) {

    override fun send(proposal: Proposal) {
        val newState = acceptor(proposal, value)
        state.send(newState)
    }

}
