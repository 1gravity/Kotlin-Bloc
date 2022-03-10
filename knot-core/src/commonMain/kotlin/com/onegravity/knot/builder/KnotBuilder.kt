package com.onegravity.knot.builder

import com.onegravity.knot.*

/**
 * The base interface for all [Knot] builders.
 */
interface KnotBuilder<State, Event, Proposal, SideEffect> {

    /** A section for [Event] related declarations. */
    fun reduce(reducer: Reducer<State, Event, Proposal, SideEffect>)

    // TODO remove this in favor of a result monad
    /** Throws [IllegalStateException] with current [State] and given [Event] in its message. */
    fun State.unexpected(event: Event): Nothing = error("Unexpected $event in $this")

    /** Turns [State] into an [Effect] without [SideEffect]. */
    fun State.toEffect(): Effect<State, SideEffect> = Effect(this)

    /** Combines [State] and [SideEffect] into [Effect]. */
    operator fun State.plus(sideEffect: SideEffect) = Effect(this, listOf(sideEffect))

}
