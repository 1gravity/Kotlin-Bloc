package com.onegravity.knot.builder

import com.onegravity.knot.*

open class SimplerKnotBuilderImpl<State, Event, Proposal> :
    SimplerKnotBuilder<State, Event, Proposal>,
    FullKnotBuilderImpl<State, Event, Proposal, SideEffect<Event>>()
{

    init {
        execute { it.block.invoke() }
    }

}