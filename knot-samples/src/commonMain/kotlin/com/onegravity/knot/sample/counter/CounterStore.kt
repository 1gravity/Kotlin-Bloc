package com.onegravity.knot.sample.counter

import org.reduxkotlin.ActionTypes
import org.reduxkotlin.createThreadSafeStore

sealed class ReduxEvent {
    data class Increment(val value: Int = 1): ReduxEvent()
    data class Decrement(val value: Int = 1): ReduxEvent()
}

internal val reduxStore = createThreadSafeStore(
    reducer = ::rootReducer,
    preloadedState = 1
)

private fun rootReducer(state: Int, action: Any) =
    when (action) {
        is ReduxEvent.Increment -> state + 1
        is ReduxEvent.Decrement -> (state - 1).coerceAtLeast(0)
        is ActionTypes.INIT -> 1
        else -> throw IllegalArgumentException("Invalid action $action")
    }
