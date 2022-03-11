package com.onegravity.knot.builder

import com.onegravity.knot.*

/**
 * A simplified [KnotBuilder] that makes the declaration of simple Knots easy:
 * - The KnotState's functionality is reduced to storing the State emitted by the Knot ([Proposal]
 *   and [State] are of the same type)
 * - The KnotState cannot be set but is generated based on an [initialState]
 * - [SideEffect] is of type SideEffect<Event> so declaration of a side effect can be
 *   ```
 *   private fun mySideEffect() = SideEffect<Event> {
 *       // do stuff (suspend function)
 *   }
 *   ```
 */
interface SimpleKnotBuilder<State, Event> : KnotBuilder<State, Event, State, SideEffect<Event>>
