package com.onegravity.knot.builder

import com.onegravity.knot.*

class KnotBuilderImpl<State, Event, Proposal, SideEffect> : KnotBuilder<State, Event, Proposal, SideEffect>() {

    override fun build(): Knot<State, Event, Proposal, SideEffect> {
        return KnotImpl(
            knotState = checkNotNull(_knotState) { "knotState must be declared" },
            reducer = checkNotNull(_reducer) { "reduce { } must be declared" },
            executor = _executor,
            dispatcherReduce = _dispatcherReduce,
            dispatcherSideEffect = _dispatcherSideEffect
        )
    }

}