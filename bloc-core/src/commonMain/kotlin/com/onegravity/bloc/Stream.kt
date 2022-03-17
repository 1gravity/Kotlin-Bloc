package com.onegravity.bloc

import kotlinx.coroutines.flow.*

/**
 * A Stream is a source of asynchronous data.
 * It's modelled after kotlinx.coroutines.flow.SharedFlow without exposing the replay cache.
 */
interface Stream<out Value> {
    /**
     * The current value of this stream
     */
    val value: Value

    /**
     * Accepts the given collector and emits values into it.
     * A Stream never completes.
     */
    suspend fun collect(collector: FlowCollector<Value>)
}
