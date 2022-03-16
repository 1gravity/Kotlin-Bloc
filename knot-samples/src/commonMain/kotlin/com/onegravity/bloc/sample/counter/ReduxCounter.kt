package com.onegravity.bloc.sample.counter

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.utils.toBlocState

object ReduxCounter {
    sealed class Event {
        data class Increment(val value: Int = 1): Event()
        data class Decrement(val value: Int = 1): Event()
    }

    fun knot(context: BlocContext) = bloc<Int, Event, ReduxEvent>(
        context,
        reduxStore.toBlocState(context, 1) { it }
    ) {
        reduce { _, event ->
            when (event) {
                is Event.Increment -> ReduxEvent.Increment(event.value)
                is Event.Decrement -> ReduxEvent.Decrement(event.value)
            }
        }
    }
}

// TODO implement the books example the classic way and using Redux, also try to load the books from a Rest API
// TODO think about the initial value, who/what provides it?
// TODO navigation, routing!
// TODO iOS
