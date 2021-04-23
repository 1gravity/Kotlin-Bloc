package com.genaku.reduce

class CoroutineKnotBuilder<S : State, C : StateIntent, A : StateAction> :
    KnotBuilder<S, C, A>() {

    private var _suspendPerformer: SuspendPerformer<A, C>? = null

    override fun build(): Knot<S, C> {
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
    fun suspendActions(performer: SuspendPerformer<A, C>?) {
        _suspendPerformer = performer
    }
}