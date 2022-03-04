package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.state.CoroutineKnotState

class EasySuspendCoroutineKnotBuilder<S : State, Intent> :
    SuspendKnotBuilder<S, Intent, SuspendSideEffect<Intent>>() {

    private val _defaultExecutor: SuspendExecutor<SuspendSideEffect<Intent>, Intent> = {
        it.block.invoke()
    }

    override fun build(): Knot<S, Intent> {
        return SuspendKnotImpl(
            knotState = _knotState ?: createKnotState(),
            reducer = checkNotNull(_reducer) { "reduce {  } must be declared" },
            performer = _executor ?: _defaultExecutor,
            dispatcher = _dispatcher
        )
    }

    private fun createKnotState() = CoroutineKnotState(
        checkNotNull(_initialState) { "initialState must be defined" }
    )
}