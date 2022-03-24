package com.onegravity.bloc.utils

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * A SideEffectStream and a Sink at the same time.
 *
 * It's a wrapper around MutableSharedFlow with a default replay cache of 0, optional initial value
 * and the extra Sink functionality.
 */
internal class MutableSideEffectStream<Value>(private val initialValue: Value?) :
    SideEffectStream<Value>,
    Sink<Value> {

    private val state = MutableSharedFlow<Value>(replay = 0)
        .apply { if (initialValue != null) tryEmit(initialValue) }

    override suspend fun collect(collector: FlowCollector<Value>) {
        state.collect(collector)
    }

    override fun send(value: Value) {
        state.tryEmit(value)
    }

}
