package com.onegravity.knot.sample.counter

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext

object ReduxCounter {
    sealed class Event {
        data class Increment(val value: Int = 1): Event()
        data class Decrement(val value: Int = 1): Event()
    }

    fun knot(context: KnotContext) = knot<Int, Event, ReduxEvent, Nothing>(
        context,
        reduxStore.toKnotState(context, 1) { state -> state }
    ) {
        reduce { _, event ->
            when (event) {
                is Event.Increment -> Effect(ReduxEvent.Increment(event.value))
                is Event.Decrement -> Effect(ReduxEvent.Decrement(event.value))
            }
        }
    }
}

// TODO think about the initial value, who/what provides it?
// TODO implement the books example the classic way and using Redux, also try to load the books from a Rest API
// TODO navigation, routing!
// TODO iOS
