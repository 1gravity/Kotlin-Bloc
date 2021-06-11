package com.genaku.reduce

class EasySuspendCoroutineKnotBuilder<S : State, C : StateIntent> :
    SuspendKnotBuilder<S, C, SuspendSideEffect<C>>() {

    private val _defaultPerformer: SuspendPerformer<SuspendSideEffect<C>, C> = {
        it.block.invoke()
    }

    override fun build(): Knot<S, C> {
        return SuspendKnotImpl(
            knotState = _knotState ?: createKnotState(),
            reducer = checkNotNull(_reducer) { "reduce {  } must be declared" },
            performer = _performer ?: _defaultPerformer,
            dispatcher = _dispatcher
        )
    }

    private fun createKnotState() = CoroutineKnotState(
        checkNotNull(_initialState) { "initialState must be defined" }
    )
}