package com.onegravity.knot.sample.counter

import co.touchlab.kermit.Logger
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.onegravity.knot.SideEffect
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knot
import com.onegravity.knot.state.ReduxKnotState
import org.reduxkotlin.ActionTypes
import org.reduxkotlin.createThreadSafeStore

object ReduxCounter {
    data class State(val count: Int = 0) {
        override fun toString() = count.toString()
    }

    sealed class Event {
        data class Increment(val value: Int = 1): Event()
        data class Decrement(val value: Int = 1): Event()
    }

    private val reduxStore = createThreadSafeStore(
        reducer = ::rootReducer,
        preloadedState = State(1)
    )

    private fun rootReducer(state: State, action: Any) =
        when (action) {
            is State -> action.copy()
            is ActionTypes.INIT -> State(1)
            else -> throw IllegalArgumentException("Invalid action $action")
        }


    fun knot(context: KnotContext) = knot<State, Event, State, SideEffect<Event>>(
        context,
        ReduxKnotState(
            context = context,
            initialState = State(1),
            store = reduxStore,
            selector = { state -> state },
            acceptor = { proposal, _ -> proposal },
            mapper = { it }
        )
    ) {
        reduce { state, event ->
            when (event) {
                is Event.Increment -> State(state.count.plus(event.value)) +
                        bonus(state.count.plus(event.value))
                is Event.Decrement -> State(state.count.minus(event.value).coerceAtLeast(0)).toEffect()
            }
        }

        execute { it.block.invoke() }
    }

    private fun bonus(value: Int) = SideEffect<Event> {
        if (value.mod(10) == 0) Event.Increment(4) else null
    }
}
