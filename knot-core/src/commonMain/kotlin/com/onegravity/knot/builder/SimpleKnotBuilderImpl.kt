package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.KnotImpl
import com.onegravity.knot.state.SimpleKnotState

class SimpleKnotBuilderImpl<State, Event> : KnotBuilder<State, Event, State, SideEffect<Event>>() {

    private val easyExecutor: Executor<SideEffect<Event>, Event> = { sideEffect ->
        sideEffect.block.invoke()
    }

    override fun build(): Knot<State, Event, State, SideEffect<Event>> {
        return KnotImpl(
            knotState = _knotState ?: defaultKnotState(),
            reducer = checkNotNull(_reducer) { "reduce {  } must be declared" },
            executor = easyExecutor,
            dispatcherReduce = _dispatcherReduce,
            dispatcherSideEffect = _dispatcherSideEffect
        )
    }

    private fun defaultKnotState() = SimpleKnotState<State>(
        checkNotNull(_initialState) { "initialState must be defined" }
    )
}
