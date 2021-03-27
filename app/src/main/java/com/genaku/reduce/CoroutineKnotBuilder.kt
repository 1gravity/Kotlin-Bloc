package com.genaku.reduce

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <State : Any, Change : Any, Action : Any> coroutineKnot(
    block: CoroutineKnotBuilder<State, Change, Action>.() -> Unit
): CoroutineKnot<State, Change, Action> = CoroutineKnotBuilder<State, Change, Action>()
    .also(block)
    .build() as CoroutineKnot<State, Change, Action>


class CoroutineKnotBuilder<State : Any, Change : Any, Action : Any> :
    KnotBuilder<State, Change, Action>() {

    private var _dispatcher: CoroutineContext = Dispatchers.Default
    private var _suspendPerformer: SuspendPerformer<Action, Change>? = null

    override fun build(): Knot<State, Change> {
        checkNotNull(_performer ?: _suspendPerformer) { "actions {  } must be declared" }
        return CoroutineKnot(
            initialState = checkNotNull(_initialState) { "initialState(initial) must be declared" },
            reducer = checkNotNull(_reducer) { "changes { reduce } must be declared" },
            performer = _performer,
            suspendPerformer = _suspendPerformer,
            dispatcher = _dispatcher
        )
    }

    /** A section for [Action] related declarations. */
    fun suspendActions(performer: SuspendPerformer<Action, Change>?) {
        _suspendPerformer = performer
    }
}