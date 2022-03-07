package com.onegravity.knot

import com.onegravity.knot.builder.*
import kotlin.jvm.JvmName

/**
 * Creates a [Knot] instance using a [FullKnotBuilder].
 */
@JvmName("fullKnot")
fun <State, Event, Proposal, SideEffect> knot(
    block: FullKnotBuilder<State, Event, Proposal, SideEffect>.() -> Unit
): Knot<State, Event, Proposal, SideEffect> =
    FullKnotBuilderImpl<State, Event, Proposal, SideEffect>()
    .also(block)
    .build()

/**
 * Creates a [Knot] instance using a [SimplerKnotBuilder]
 */
@JvmName("simplerKnot")
fun <State, Event, Proposal> knot(
    block: SimplerKnotBuilder<State, Event, Proposal>.() -> Unit
): Knot<State, Event, Proposal, SideEffect<Event>> =
    SimplerKnotBuilderImpl<State, Event, Proposal>()
        .also(block)
        .build()

/**
 * Creates a [Knot] instance using a [SimplestKnotBuilder].
 */
@JvmName("simplestKnot")
fun <State, Event> knot(
    block: SimplestKnotBuilder<State, Event>.() -> Unit
): Knot<State, Event, State, SideEffect<Event>> =
    SimplestKnotBuilderImpl<State, Event>()
    .also(block)
    .build()
