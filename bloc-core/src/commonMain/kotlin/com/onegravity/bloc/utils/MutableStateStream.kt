package com.onegravity.bloc.utils

import com.onegravity.bloc.Sink
import com.onegravity.bloc.StateStream
import kotlinx.coroutines.flow.*

/**
 * A StateStream and a Sink at the same time.
 *
 * It's a wrapper around MutableStateFlow with the extra Sink functionality.
 */
public class MutableStateStream<Value : Any>(initialValue: Value) :
    StateStream<Value>,
    Sink<Value> {

    private val state = MutableStateFlow(initialValue)

    override val value: Value
        get() = state.value

    override suspend fun collect(collector: FlowCollector<Value>) {
        state.collect(collector)
    }

    override fun send(value: Value) {
        state.update { value }
    }

}
