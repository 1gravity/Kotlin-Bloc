package com.onegravity.knot.builder

import com.onegravity.knot.*

/** The base interface for all configuration builders for a [Knot]. */
interface KnotBuilder<State, Event, Proposal, SideEffect> {

    fun build(): Knot<State, Event, Proposal, SideEffect>

    /** A section for [Event] related declarations. */
    fun reduce(reducer: Reducer<State, Event, Proposal, SideEffect>)

    // TODO remove this in favor of a result monad
    /** Throws [IllegalStateException] with current [State] and given [Event] in its message. */
    fun State.unexpected(action: Event): Nothing = error("Unexpected $action in $this")

    /** Turns [State] into an [Effect] without [SideEffect]. */
    fun State.toEffect(): Effect<State, SideEffect> = Effect(this)

    /** Combines [State] and [SideEffect] into [Effect]. */
    operator fun State.plus(sideEffect: SideEffect) = Effect(this, listOf(sideEffect))

}

// TODO re-implement this
///**
// * Executes given block if the knot is in the given state or
// * ignores the event in any other states.
// *
// * ```
// * reduce<Event> { state, event ->
// *    whenState<State.Content> {
// *       ...
// *    }
// * }
// * ```
// * is a better readable alternative to
// * ```
// * reduce { state, event ->
// *    when(state) {
// *       is State.Content -> ...
// *       else -> only
// *    }
// * }
// * ```
// */
//inline fun <reified WhenState : State> State.whenState(
//    block: WhenState.() -> Effect<State, SideEffect>
//): Effect<State, SideEffect> =
//    if (this is WhenState) block()
//    else Effect(this, emptyList())
//
///**
// * Executes given block if the knot is in the given state or
// * throws [IllegalStateException] for the event in any other state.
// *
// * ```
// * reduce { state, event ->
// *    requireState<State.Content>(event) {
// *       ...
// *    }
// * }
// * ```
// * is a better readable alternative to
// * ```
// * reduce { state, event ->
// *    when(state) {
// *       is State.Content -> ...
// *       else -> unexpected(event)
// *    }
// * }
// * ```
// */
//inline fun <reified WhenState : State> State.requireState(
//    action: Event, block: WhenState.() -> Effect<State, SideEffect>
//): Effect<State, SideEffect> =
//    if (this is WhenState) block()
//    else unexpected(action)
