package com.genaku.reduce

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class EasySuspendCoroutineKnotBuilder<S : State, C : Intent> :
    KnotBuilder<S, C, SuspendSideEffect<C>>() {

    private val _suspendPerformer: SuspendPerformer<SuspendSideEffect<C>, C> = {
        it.block.invoke()
    }

    override fun build(): Knot<S, C> {
        return KnotImpl(
            knotState = _knotState ?: createKnotState(),
            reducer = checkNotNull(_reducer) { "changes {  } must be declared" },
            performer = _performer,
            suspendPerformer = _suspendPerformer,
            dispatcher = _dispatcher
        )
    }

    private fun createKnotState() = CoroutineKnotState(
        checkNotNull(_initialState) { "initialState must be defined" }
    )
}