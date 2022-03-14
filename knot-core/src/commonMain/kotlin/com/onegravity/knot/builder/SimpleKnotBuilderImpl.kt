package com.onegravity.knot.builder

import com.onegravity.knot.*

class SimpleKnotBuilderImpl<State, Event> : SimpleKnotBuilder<State, Event>,
    FullKnotBuilderImpl<State, Event, State, SideEffect<Event>>() {

    init {
        execute { it.block.invoke() }
    }

}