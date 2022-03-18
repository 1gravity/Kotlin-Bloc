package com.onegravity.bloc.utils

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * A Stream and a Sink at the same time. It's basically a wrapper around MutableSharedFlow with
 * a default replay cache of 1, enforced initial value and a way to retrieve the last/current value.
 * With the default replay value of 1, this would be a BehaviorSubject in Rx terms.
 * With a replay value of 0, this would be a PublishSubject in Rx terms.
 */
class MutableStream<Value>(
    private val initialValue: Value,
    replay: Int = 1
) : StateStream<Value>, Sink<Value> {

    override val replayCache: List<Value>
        get() = state.replayCache

    private val state = MutableSharedFlow<Value>(replay)
        .apply { tryEmit(initialValue) }

    override val value: Value
        get() = state.replayCache.first()

    override suspend fun collect(collector: FlowCollector<Value>): Nothing {
        state.collect(collector)
    }

    override fun emit(value: Value) {
        state.tryEmit(value)
    }

}