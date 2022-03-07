package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.state.KnotState
import kotlin.coroutines.CoroutineContext

/**
 * A [KnotBuilder] that allows the configuration of every [Knot] attribute/functionality.
 */
interface FullKnotBuilder<State, Event, Proposal, SideEffect> :
    KnotBuilder<State, Event, Proposal, SideEffect> {

    /** A section for [Event] related declarations.*/
    fun execute(executor: Executor<SideEffect, Event>?)

    /** Set the [KnotState]. */
    var knotState: KnotState<State, Proposal>

    /** Set coroutine context dispatcher for the reduce function. */
    var dispatcherReduce: CoroutineContext

    /** Set coroutine context dispatcher for the execute function. */
    var dispatcherSideEffect: CoroutineContext

}
