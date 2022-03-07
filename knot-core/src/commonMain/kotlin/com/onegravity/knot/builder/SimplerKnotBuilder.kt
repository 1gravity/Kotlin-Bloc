package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.state.KnotState
import kotlin.coroutines.CoroutineContext

/**
 * Convenience [KnotBuilder] allowing the use of the simplified syntax for side effects:
 *
 * [SideEffect] is of type SideEffect<Event> so declaration of a side effect can be
 *   ```
 *   private fun mySideEffect(): SideEffect<Event> = SideEffect {
 *       // do stuff (suspend function)
 *   }
 *   ```
 */
interface SimplerKnotBuilder<State, Event, Proposal> :
    KnotBuilder<State, Event, Proposal, SideEffect<Event>> {

    /** Set the [KnotState]. */
    var knotState: KnotState<State, Proposal>

    /** Set coroutine context dispatcher for the reduce function. */
    var dispatcherReduce: CoroutineContext

    /** Set coroutine context dispatcher for the execute function. */
    var dispatcherSideEffect: CoroutineContext

}
