package com.onegravity.knot

import com.onegravity.knot.builder.*
import com.onegravity.knot.state.*
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

/**
 * Creates a [KnotState] instance using a [SimpleKnotStateBuilder]
 */
@JvmName("simpleKnotState")
fun <State> knotState(
    block: SimpleKnotStateBuilder<State>.() -> Unit
): KnotState<State, State> =
    SimpleKnotStateBuilderImpl<State>()
        .also(block)
        .build()

/**
 * Similar to the previous [knotState] function but takes the initialState as function parameter to
 * simplify from
 * ```
 *    knotState<State> { initialState = SomeState }
 * ```
 * to
 * ```
 *    knotState<State>(SomeState)
 * ```
 */
@JvmName("simpleKnotStateWithInitialState")
fun <State> knotState(
    initialState: State
): KnotState<State, State> =
    SimpleKnotStateBuilderImpl<State>()
        .also { it.initialState = initialState }
        .build()

/**
 * Creates a [KnotState] instance using a [KnotStateBuilder]
 */
@JvmName("knotState")
fun <State, Proposal> knotState(
    block: KnotStateBuilder<State, Proposal>.() -> Unit
): KnotState<State, Proposal> =
    KnotStateBuilderImpl<State, Proposal>()
        .also(block)
        .build()

/**
 * Creates a [KnotState] instance using a [FullKnotBuilder]
 */
@JvmName("knotKnotState")
fun <State, Proposal, SideEffect> knotState(
    block: FullKnotBuilder<State, Proposal, Proposal, SideEffect>.() -> Unit
): Knot<State, Proposal, Proposal, SideEffect> =
    FullKnotBuilderImpl<State, Proposal, Proposal, SideEffect>()
        .also(block)
        .build()
