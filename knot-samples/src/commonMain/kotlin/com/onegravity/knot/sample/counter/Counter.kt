package com.onegravity.knot.sample.counter

import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knot

sealed class Event {
    object Increment: Event()
    object Decrement: Event()
}

fun counterKnot(context: KnotContext) = knot<Int, Event>(context, 0) {
    reduce { state, event ->
        when (event) {
            Event.Increment -> state.inc().toEffect()
            Event.Decrement -> state.dec().toEffect()
        }
    }
}
