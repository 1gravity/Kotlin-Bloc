package com.onegravity.knot.sample.counter

import com.onegravity.knot.SideEffect
import com.onegravity.bloc.context.BlocContext
import com.onegravity.knot.knot

object SimpleCounter {
    sealed class Event {
        data class Increment(val value: Int = 1): Event()
        data class Decrement(val value: Int = 1): Event()
    }

    fun knot(context: BlocContext) = knot<Int, Event>(context, 1) {
        reduce { state, event ->
            when (event) {
                is Event.Increment -> state.plus(event.value) + bonus(state.plus(event.value))
                is Event.Decrement -> state.minus(event.value).coerceAtLeast(0).toEffect()
            }
        }
    }

    private fun bonus(value: Int) = SideEffect<Event> {
        if (value.mod(10) == 0) Event.Increment(4) else null
    }
}
