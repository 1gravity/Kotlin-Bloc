package com.onegravity.bloc.state

import com.onegravity.bloc.utils.MutableStream
import com.onegravity.bloc.utils.Acceptor
import com.onegravity.bloc.utils.logger
import kotlinx.coroutines.flow.FlowCollector

open class BlocStateImpl<State, Proposal>(
    initialState: State,
    private val acceptor: Acceptor<Proposal, State>,
) : BlocState<State, Proposal> {

    private val state = MutableStream(initialState, 1)

    /**
     * The Stream<State>.
     */
    override val value: State
        get() = state.value

    override val replayCache: List<State>
        get() = state.replayCache

    override suspend fun collect(collector: FlowCollector<State>): Nothing {
        state.collect(collector)
    }

    /**
     * The Sink<Proposal>.
     */
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun emit(proposal: Proposal) {
        logger.d("BlocState proposal: $proposal")
        val newState = acceptor(proposal, value)
        state.emit(newState)
    }

}
