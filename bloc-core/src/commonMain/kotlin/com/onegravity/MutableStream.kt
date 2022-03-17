package com.onegravity

import com.onegravity.bloc.Sink
import com.onegravity.bloc.Stream
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * A Stream and a Sink at the same time. It's basically a wrapper around MutableSharedFlow with
 * a replay cache of 1, enforced initial value and a way to retrieve the last/current value.
 * In Rx terms, this would be a BehaviorSubject.
 */
class MutableStream<Value>(private val initialValue: Value) : Stream<Value>, Sink<Value> {

    private val state = MutableSharedFlow<Value>(1)
        .apply { tryEmit(initialValue) }

    override val value: Value
        get() = state.replayCache.first()

    override suspend fun collect(collector: FlowCollector<Value>) {
        state.collect(collector)
    }

    override fun emit(value: Value) {
        state.tryEmit(value)
    }

}