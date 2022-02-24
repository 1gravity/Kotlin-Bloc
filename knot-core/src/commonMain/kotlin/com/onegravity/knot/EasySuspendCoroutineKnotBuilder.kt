package com.onegravity.knot

class EasySuspendCoroutineKnotBuilder<S : State, Intent> :
    SuspendKnotBuilder<S, Intent, SuspendSideEffect<Intent>>() {

    private val _defaultPerformer: SuspendPerformer<SuspendSideEffect<Intent>, Intent> = {
        it.block.invoke()
    }

    override fun build(): Knot<S, Intent> {
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