package com.onegravity.knot.builder

import com.onegravity.knot.SideEffect
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knotState

class SimplestKnotBuilderImpl<State, Event>(
    override val context: KnotContext
) : SimplestKnotBuilder<State, Event>,
    FullKnotBuilderImpl<State, Event, State, SideEffect<Event>>(context)
{

    init {
        execute { it.block.invoke() }
    }

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            knotState = knotState(value)
        }

}
