package com.onegravity.bloc

import kotlinx.coroutines.flow.FlowCollector

/**
 * A StateStream is a source of asynchronous (state) data.
 * It's a hot stream, identical to kotlinx.coroutines.flow.StateFlow without exposing the
 * replayCache and meant to deal with State data (compared to SideEffectStream for SideEffects).
 *
 * A StateStream emits:
 * - no duplicate values
 * - an initial value upon subscription (analogous BehaviorSubject)
 *
 * A SideEffectStream emits:
 * - all values even duplicates
 * - no initial value upon subscription (analogous PublishSubject)
 */
public interface StateStream<out Value : Any> {
    /**
     * The current value.
     */
    public val value: Value

    /**
     * Accepts the given FlowCollector and emits values into it.
     */
    public suspend fun collect(collector: FlowCollector<Value>)
}
