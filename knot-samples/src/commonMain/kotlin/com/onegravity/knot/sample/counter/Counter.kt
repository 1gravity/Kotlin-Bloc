package com.onegravity.knot.sample.counter

import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knot

object Counter {
    sealed class Event {
        object Increment: Event()
        object Decrement: Event()
    }

    fun knot(context: KnotContext) = knot<Int, Event>(context, 0) {
        reduce { state, event ->
            when (event) {
                Event.Increment -> state.inc().toEffect()
                Event.Decrement -> state.dec().coerceAtLeast(0).toEffect()
            }
        }
    }
}
