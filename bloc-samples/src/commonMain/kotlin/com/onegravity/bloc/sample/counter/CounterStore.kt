package com.onegravity.bloc.sample.counter

import org.reduxkotlin.createThreadSafeStore

object CounterStore {
    sealed class ReduxAction {
        data class Increment(val value: Int = 1): ReduxAction()
        data class Decrement(val value: Int = 1): ReduxAction()
    }

    /**
     * You can also write:
     * ```
     * val reducer = { state: Int, action: Any ->
     *    // etc.
     * }
     */
    private fun reducer(state: Int, action: Any) = when (action) {
        is ReduxAction.Increment -> state + 1
        is ReduxAction.Decrement -> (state - 1).coerceAtLeast(0)
        else -> state
    }

    internal val reduxStore = createThreadSafeStore(::reducer, 1)
}
