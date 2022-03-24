package com.onegravity.bloc.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector


/**
 * A StateStream is a source of asynchronous (state) data.
 * It's identical to kotlinx.coroutines.flow.StateFlow without exposing the replayCache.
 * It's meant to deal with State data (compared to a SideEffectStream which deals with SideEffects).
 *
 * A StateStream emits:
 * - no duplicate values
 * - an initial value upon subscription (analogous BehaviorSubject)
 *
 * A SideEffectStream emits:
 * - all values even duplicates
 * - no initial value upon subscription (analogous PublishSubject)
 */
interface StateStream<out Value> {
    val value: Value
    suspend fun collect(collector: FlowCollector<Value>)
}