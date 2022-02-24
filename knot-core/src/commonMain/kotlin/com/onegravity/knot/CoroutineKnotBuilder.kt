package com.onegravity.knot

class CoroutineKnotBuilder<S : State, Intent, SideEffect> :
    KnotBuilder<S, Intent, SideEffect>() {

    private var _suspendPerformer: SuspendPerformer<SideEffect, Intent>? = null

    override fun build(): Knot<S, Intent> {
        return KnotImpl(
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