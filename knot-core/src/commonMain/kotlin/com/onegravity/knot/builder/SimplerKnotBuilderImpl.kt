package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext

open class SimplerKnotBuilderImpl<State, Event, Proposal>(
    override val context: KnotContext
) : SimplerKnotBuilder<State, Event, Proposal>,
    FullKnotBuilderImpl<State, Event, Proposal, SideEffect<Event>>(context)
{

    init {
        execute { it.block.invoke() }
    }

}