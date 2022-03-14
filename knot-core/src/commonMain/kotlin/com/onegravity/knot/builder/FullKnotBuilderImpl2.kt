package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.KnotState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class FullKnotBuilderImpl2<State, Action, Proposal: Any> {

    private var _reducer: Reducer2<State, Action, Proposal>? = null
    private var _dispatcherReduce: CoroutineContext = Dispatchers.Default

    fun build(context: KnotContext, knotState: KnotState<State, Proposal>) = KnotImpl2(
        context = context,
        knotState = knotState,
        reducer = checkNotNull(_reducer) { "reduce { } must be declared" },
        dispatcherReduce = _dispatcherReduce,
    )

    /** A section for [Event] related declarations. */
    fun reduce(reducer: Reducer2<State, Action, Proposal>) {
        _reducer = reducer
    }

    var dispatcherReduce: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _dispatcherReduce = value
        }

}