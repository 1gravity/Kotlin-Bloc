package com.onegravity.bloc.utils

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
interface StateStream<out Value: Any> {
    val value: Value
    suspend fun collect(collector: FlowCollector<Value>)
}