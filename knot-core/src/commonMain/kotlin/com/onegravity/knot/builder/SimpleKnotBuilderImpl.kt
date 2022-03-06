package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.KnotImpl
import com.onegravity.knot.state.SimpleKnotState

class SimpleKnotBuilderImpl<State, Event> : KnotBuilder<State, Event, State, SideEffect<Event>>() {

    override fun build(): Knot<State, Event, State, SideEffect<Event>> {
        return KnotImpl(
            knotState = _knotState ?: easyKnotState,
            reducer = checkNotNull(_reducer) { "reduce {  } must be declared" },
            executor = easyExecutor,
            dispatcherReduce = _dispatcherReduce,
            dispatcherSideEffect = _dispatcherSideEffect
        )
    }

    private val easyKnotState by lazy {
        SimpleKnotState<State>(checkNotNull(_initialState) { "initialState must be defined" })
    }

    private val easyExecutor: Executor<SideEffect<Event>, Event> = { sideEffect ->
        sideEffect.block.invoke()
    }

}
