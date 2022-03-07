package com.onegravity.knot.builder

import com.onegravity.knot.SideEffect
import com.onegravity.knot.state.SimpleKnotState

class SimplestKnotBuilderImpl<State, Event> :
    SimplestKnotBuilder<State, Event>,
    FullKnotBuilderImpl<State, Event, State, SideEffect<Event>>()
{

    init {
        execute { it.block.invoke() }
    }

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            knotState = SimpleKnotState(value)
        }

}
