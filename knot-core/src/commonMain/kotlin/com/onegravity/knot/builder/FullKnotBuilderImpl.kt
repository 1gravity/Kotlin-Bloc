package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.KnotState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class FullKnotBuilderImpl<State, Event, Proposal, SideEffect>(
    override val context: KnotContext
) : FullKnotBuilder<State, Event, Proposal, SideEffect> {

    private var _knotState: KnotState<State, Proposal>? = null
    private var _reducer: Reducer<State, Event, Proposal, SideEffect>? = null
    private var _executor: Executor<SideEffect, Event>? = null
    private var _dispatcherReduce: CoroutineContext = Dispatchers.Default
    private var _dispatcherSideEffect: CoroutineContext = Dispatchers.Default

    override fun build() = KnotImpl(
        context = context,
        knotState = checkNotNull(_knotState) { "knotState must be declared" },
        reducer = checkNotNull(_reducer) { "reduce { } must be declared" },
        executor = _executor,
        dispatcherReduce = _dispatcherReduce,
        dispatcherSideEffect = _dispatcherSideEffect
    )

    /** Set the initial state. */
    override var knotState: KnotState<State, Proposal>
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _knotState = value
        }

    /** A section for [Event] related declarations. */
    final override fun reduce(reducer: Reducer<State, Event, Proposal, SideEffect>) {
        _reducer = reducer
    }

    /** A section for [Event] related declarations. */
    final override fun execute(executor: Executor<SideEffect, Event>?) {
        _executor = executor
    }

    /** Set coroutine context dispatcher for the reduce function */
    final override var dispatcherReduce: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _dispatcherReduce = value
        }

    /** Set coroutine context dispatcher for the execute function */
    final override var dispatcherSideEffect: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _dispatcherSideEffect = value
        }

}