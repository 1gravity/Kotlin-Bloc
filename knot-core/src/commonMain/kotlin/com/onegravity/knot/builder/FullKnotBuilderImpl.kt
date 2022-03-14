package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.BlocState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class FullKnotBuilderImpl<State, Event, Proposal, SideEffect> : FullKnotBuilder<State, Event, Proposal, SideEffect> {

    private var _reducer: Reducer<State, Event, Proposal, SideEffect>? = null
    private var _executor: Executor<SideEffect, Event>? = null
    private var _dispatcherReduce: CoroutineContext = Dispatchers.Default
    private var _dispatcherSideEffect: CoroutineContext = Dispatchers.Default

    fun build(context: BlocContext, knotState: BlocState<State, Proposal>) = KnotImpl(
        context = context,
        knotState = knotState,
        reducer = checkNotNull(_reducer) { "reduce { } must be declared" },
        executor = _executor,
        dispatcherReduce = _dispatcherReduce,
        dispatcherSideEffect = _dispatcherSideEffect
    )

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