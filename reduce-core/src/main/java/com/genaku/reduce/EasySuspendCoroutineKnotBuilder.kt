package com.genaku.reduce

class EasySuspendCoroutineKnotBuilder<S : State, C : StateIntent> :
    KnotBuilder<S, C, SuspendSideEffect<C>>() {

    private val _suspendPerformer: SuspendPerformer<SuspendSideEffect<C>, C> = {
        it.block.invoke()
    }

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
}