package com.onegravity.bloc.utils

import com.onegravity.bloc.Sink
import com.onegravity.bloc.StateStream
import kotlinx.coroutines.flow.FlowCollector

/**
 * A StateStream and a Sink at the same time.
 *
 * It's mostly a wrapper around MutableSharedFlow with some extra "features".
 */
public class MutableStateStream<Value : Any>(initialValue: Value) :
    StateStream<Value>,
    Sink<Value> {

    private val state = MutableBehaviorFlow(initialValue)

    override val value: Value
        get() = state.value

    override suspend fun collect(collector: FlowCollector<Value>) {
        state.collect(collector)
    }

    override fun send(value: Value) {
        state.tryEmit(value)
    }

}
