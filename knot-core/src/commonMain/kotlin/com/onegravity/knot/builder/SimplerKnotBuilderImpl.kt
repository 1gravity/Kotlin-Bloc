package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.KnotState

open class SimplerKnotBuilderImpl<State, Event, Proposal>(
    override val _context: KnotContext,
    _knotState: KnotState<State, Proposal>
) : SimplerKnotBuilder<State, Event, Proposal>,
    FullKnotBuilderImpl<State, Event, Proposal, SideEffect<Event>>(_context, _knotState)
{

    init {
        execute { it.block.invoke() }
    }

}