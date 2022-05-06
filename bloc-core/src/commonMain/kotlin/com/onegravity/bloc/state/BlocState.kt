package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Sink
import com.onegravity.bloc.utils.StateStream
import kotlinx.coroutines.flow.FlowCollector

/**
 * BlocState is the actual keeper of State. a source of asynchronous state data (StateStream) and a
 * Sink for proposals used to potentially alter state.
 */
abstract class BlocState<out State : Any, in Proposal : Any> : StateStream<State>, Sink<Proposal> {
    /**
     * StateStream
     */
    abstract override val value: State

    abstract override suspend fun collect(collector: FlowCollector<State>)

    /**
     * Sink
     */
    abstract override fun send(value: Proposal)
}