package com.onegravity.knot

class EasyCoroutineKnotBuilder<S : State, Intent> :
    KnotBuilder<S, Intent, SideEffect<Intent>>() {

    private val _suspendPerformer: SuspendPerformer<SideEffect<Intent>, Intent> = {
        it.block.invoke()
    }

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
}