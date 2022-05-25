package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Sink
import com.onegravity.bloc.utils.StateStream
import kotlinx.coroutines.flow.FlowCollector

/**
 * BlocState is the actual keeper of State. a source of asynchronous state data (StateStream) and a
 * Sink for proposals used to potentially alter state.
 */
public abstract class BlocState<out State : Any, in Proposal : Any> : StateStream<State>, Sink<Proposal> {
    /**
     * StateStream
     */
    public abstract override val value: State

    public abstract override suspend fun collect(collector: FlowCollector<State>)

    /**
     * Sink
     */
    public abstract override fun send(value: Proposal)
}