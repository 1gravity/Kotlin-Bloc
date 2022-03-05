package com.onegravity.knot.state

import com.onegravity.knot.Acceptor
import com.onegravity.knot.Effect
import com.onegravity.knot.Mapper
import com.onegravity.knot.SideEffect
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

open class KnotStateImpl<State, Proposal, Model>(
    initialState: State,
    private val acceptor: Acceptor<State, Proposal, Model>,
    private val mapper: Mapper<Model, State>
) : KnotState<State, Proposal>,
    DisposableKnotState() {

    private val state = MutableStateFlow(initialState)

    /**
     * The Stream<State>.
     */
    override val value: State
        get() = state.value

    override suspend fun collect(collector: FlowCollector<State>): Nothing {
        state.collect(collector)
    }

    /**
     * The Sink<Proposal>.
     */
    override fun emit(value: Proposal) {
        val model = acceptor(state.value, value)
        val newState = mapper(model)
        state.tryEmit(newState)
    }

}

