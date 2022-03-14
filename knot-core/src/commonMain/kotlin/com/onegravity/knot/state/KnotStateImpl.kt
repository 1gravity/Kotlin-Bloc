package com.onegravity.knot.state

import co.touchlab.kermit.Logger
import com.onegravity.bloc.BlocState
import com.onegravity.knot.Acceptor
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

open class KnotStateImpl<State, Proposal>(
    initialState: State,
    private val acceptor: Acceptor<Proposal, State>,
) : BlocState<State, Proposal> {

    private val state = MutableStateFlow(initialState)

    /**
     * The Stream<Model>.
     */
    override val value: State
        get() = state.value

    override suspend fun collect(collector: FlowCollector<State>) {
        state.collect(collector)
    }

    /**
     * The Sink<Proposal>.
     */
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun emit(proposal: Proposal) {
        Logger.withTag("knot").d("KnotState proposal: $proposal")
        val newState = acceptor(proposal, value)
        state.tryEmit(newState)
    }
}
