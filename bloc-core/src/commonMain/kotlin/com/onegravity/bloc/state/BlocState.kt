package com.onegravity.bloc.state

import com.onegravity.bloc.utils.*
import kotlinx.coroutines.flow.FlowCollector

/**
 * BlocState is the actual keeper of State. a source of asynchronous state data (StateStream) and a
 * Sink for proposals used to potentially alter state.
 */
public abstract class BlocState<out State : Any, in Proposal : Any> : StateStream<State>, Sink<Proposal> {

    /**
     * StateStream
     */
    abstract override val value: State

    abstract override suspend fun collect(collector: FlowCollector<State>)

    /**
     * Sink
     */
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    abstract override fun send(proposal: Proposal)

}