package com.onegravity.knot.builder

import com.onegravity.knot.*

/**
 * TODO move all knot code into a shared module
 * TODO rename everything to BLoC
 * TODO implement Redux KnotStore + Sample App
 * TODO implement the orbit demo app in BLoC
 * TODO the easyKnot builder with / without SideEffect<Event>
 * TODO the knot builder with / without SideEffect<Event>
 * TODO convert dispatcher functions into properties in the KnotBuilder
 */

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