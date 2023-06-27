package com.onegravity.bloc.state

import com.onegravity.bloc.Sink
import com.onegravity.bloc.StateStream
import kotlinx.coroutines.flow.FlowCollector

/**
 * BlocState is the actual keeper of State. a source of asynchronous state data (StateStream) and a
 * Sink for proposals used to potentially alter state.
 *
 * It needs to be a class so generic types aren't erased in Swift.
 */
public abstract class BlocState<out State : Any, in Proposal : Any> :
    StateStream<State>,
    Sink<Proposal> {

    /**
     * StateStream.value
     */
    abstract override val value: State

    /**
     * StateStream.collect(FlowCollector<Value>)
     */
    abstract override suspend fun collect(collector: FlowCollector<State>)

    /**
     * Sink.send(Proposal)
     */
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    abstract override fun send(proposal: Proposal)

}
