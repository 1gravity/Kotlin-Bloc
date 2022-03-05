package com.onegravity.knot

import com.onegravity.knot.builder.KnotBuilderImpl
import com.onegravity.knot.builder.SimpleKnotBuilderImpl

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <State, Event, Proposal, SideEffect> knot(
    block: KnotBuilderImpl<State, Event, Proposal, SideEffect>.() -> Unit
): KnotImpl<State, Event, Proposal, SideEffect> =
    KnotBuilderImpl<State, Event, Proposal, SideEffect>()
    .also(block)
    .build() as KnotImpl<State, Event, Proposal, SideEffect>

@Suppress("UNCHECKED_CAST")
fun <State, Event> simpleKnot(
    block: SimpleKnotBuilderImpl<State, Event>.() -> Unit
): KnotImpl<State, Event, State, SideEffect<Event>> =
    SimpleKnotBuilderImpl<State, Event>()
    .also(block)
    .build() as KnotImpl<State, Event, State, SideEffect<Event>>
