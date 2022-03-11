package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.KnotState

class SimpleKnotBuilderImpl<State, Event> : SimpleKnotBuilder<State, Event>,
    FullKnotBuilderImpl<State, Event, State, SideEffect<Event>>() {

    init {
        execute { it.block.invoke() }
    }

}