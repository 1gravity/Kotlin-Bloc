package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor
import com.onegravity.bloc.utils.MutableStateStream
import kotlinx.coroutines.flow.FlowCollector

internal open class BlocStateImpl<State : Any, Proposal : Any>(
    initialState: State,
    private val acceptor: Acceptor<Proposal, State>,
) : BlocState<State, Proposal>() {

    private val state = MutableStateStream(initialState)

    /**
     * The Stream<State>.
     */
    override val value: State
        get() = state.value

    override suspend fun collect(collector: FlowCollector<State>) {
        state.collect(collector)
    }

    /**
     * The Sink<Proposal>.
     */
    override fun send(proposal: Proposal) {
        val newState = acceptor(proposal, value)
        state.send(newState)
    }

}
