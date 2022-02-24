package com.onegravity.knot

class CoroutineKnotBuilder<S : State, Intent, A : StateAction> :
    KnotBuilder<S, Intent, A>() {

    private var _suspendPerformer: SuspendPerformer<A, Intent>? = null

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

    /** A section for [A] related declarations. */
    fun suspendActions(performer: SuspendPerformer<A, Intent>?) {
        _suspendPerformer = performer
    }
}