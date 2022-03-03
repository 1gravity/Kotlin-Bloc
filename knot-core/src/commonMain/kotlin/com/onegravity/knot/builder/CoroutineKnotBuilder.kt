package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.state.CoroutineKnotState
import com.onegravity.knot.SimpleKnotImpl

class CoroutineKnotBuilder<State, Intent, SideEffect> :
    KnotBuilder<State, Intent, SideEffect>() {

    private var _suspendPerformer: SuspendPerformer<SideEffect, Intent>? = null

    override fun build(): Knot<Intent, State, State, SideEffect> {
        return SimpleKnotImpl(
            knotState = _knotState ?: createKnotState(),
            reducer = checkNotNull(_reducer) { "reduce {  } must be declared" },
            performer = _performer,
            suspendPerformer = _suspendPerformer,
            dispatcher = _dispatcher
        )
    }

    private fun createKnotState() = CoroutineKnotState(
        checkNotNull(_initialState) { "initialState must be defined" }
    )

    /** SideEffect section for [SideEffect] related declarations. */
    fun suspendActions(performer: SuspendPerformer<SideEffect, Intent>?) {
        _suspendPerformer = performer
    }
}