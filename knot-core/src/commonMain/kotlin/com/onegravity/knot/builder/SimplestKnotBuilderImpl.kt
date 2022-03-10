package com.onegravity.knot.builder

import com.onegravity.knot.SideEffect
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knotState

class SimplestKnotBuilderImpl<State, Event>(
    override val _context: KnotContext,
    _initialState: State
) : SimplestKnotBuilder<State, Event>,
    FullKnotBuilderImpl<State, Event, State, SideEffect<Event>>(_context, knotState(_initialState))
{

    init {
        execute { it.block.invoke() }
    }

}
