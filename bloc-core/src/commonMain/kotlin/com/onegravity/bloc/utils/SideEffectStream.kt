package com.onegravity.bloc.utils

import kotlinx.coroutines.flow.Flow

/**
 * A SideEffectStream is a source of asynchronous (side effect) data.
 * It's meant to deal with SideEffect data (compared to a StateStream which deals with States).
 *
 * A SideEffectStream emits:
 * - all values even duplicates
 * - no initial value upon subscription (analogous PublishSubject)
 *
 * A StateStream emits:
 * - no duplicate values
 * - an initial value upon subscription (analogous BehaviorSubject)
 */
interface SideEffectStream<out Value> : Flow<Value>