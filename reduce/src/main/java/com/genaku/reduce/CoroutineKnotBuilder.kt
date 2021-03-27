package com.genaku.reduce

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CoroutineKnotBuilder<State : Any, Change : Any, Action : Any> :
    KnotBuilder<State, Change, Action>() {

    private var _dispatcher: CoroutineContext = Dispatchers.Default
    private var _suspendPerformer: SuspendPerformer<Action, Change>? = null

    override fun build(): Knot<State, Change> {
        return CoroutineKnot(
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

    /** A section for [Action] related declarations. */
    fun suspendActions(performer: SuspendPerformer<Action, Change>?) {
        _suspendPerformer = performer
    }
}