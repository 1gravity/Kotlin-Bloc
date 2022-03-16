package com.onegravity.bloc.sample.counter

import org.reduxkotlin.createThreadSafeStore

sealed class ReduxEvent {
    data class Increment(val value: Int = 1): ReduxEvent()
    data class Decrement(val value: Int = 1): ReduxEvent()
}

/**
 * You can also write:
 * ```
 * val reducer = { state: Int, action: Any ->
 *    // etc.
 * }
 */
private fun reducer(state: Int, action: Any) = when (action) {
    is ReduxEvent.Increment -> state + 1
    is ReduxEvent.Decrement -> (state - 1).coerceAtLeast(0)
    else -> state
}

internal val reduxStore = createThreadSafeStore(::reducer, 1)
