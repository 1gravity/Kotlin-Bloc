package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.KnotImpl
import com.onegravity.knot.state.KnotStateImpl
import com.onegravity.knot.state.SimpleKnotState

class KnotBuilderImpl<State, Event, Proposal, SideEffect> : KnotBuilder<State, Event, Proposal, SideEffect>() {

    override fun build(): Knot<State, Event, Proposal, SideEffect> {
        checkNotNull(_reducer ?: _suspendReducer) { "reduce {  } or suspendReduce {  } must be declared" }
        return KnotImpl(
            knotState = _knotState ?: createKnotState(),
            reducer = _reducer,
            suspendReducer = _suspendReducer,
            executor = _executor,
            suspendExecutor = _suspendExecutor,
            dispatcherReduce = _dispatcherReduce,
            dispatcherSideEffect = _dispatcherSideEffect
        )
    }

    private fun createKnotState() = KnotStateImpl<State, Proposal, State>(
        checkNotNull(_initialState) { "initialState must be defined" }
    )

}