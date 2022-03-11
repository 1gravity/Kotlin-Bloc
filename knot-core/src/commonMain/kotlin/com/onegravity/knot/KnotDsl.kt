package com.onegravity.knot

import com.onegravity.knot.builder.*
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.*
import org.reduxkotlin.Store
import kotlin.jvm.JvmName

/**
 * Creates a [Knot] instance using a [FullKnotBuilder].
 */
@JvmName("fullKnot")
fun <State, Event, Proposal, SideEffect> knot(
    context: KnotContext,
    knotState: KnotState<State, Proposal>,
    block: FullKnotBuilder<State, Event, Proposal, SideEffect>.() -> Unit
): Knot<State, Event, Proposal, SideEffect> =
    FullKnotBuilderImpl<State, Event, Proposal, SideEffect>()
    .also(block)
    .build(context, knotState)

/**
 * Creates a [Knot] instance using a [SimpleKnotBuilder]
 */
@JvmName("simplerKnot")
fun <State, Event> knot(
    context: KnotContext,
    knotState: KnotState<State, State>,
    block: SimpleKnotBuilder<State, Event>.() -> Unit
): Knot<State, Event, State, SideEffect<Event>> =
    SimpleKnotBuilderImpl<State, Event>()
        .also(block)
        .build(context, knotState)

/**
 * Creates a [Knot] instance using a [SimpleKnotBuilder].
 */
@JvmName("simplestKnot")
fun <State, Event> knot(
    context: KnotContext,
    initialState: State,
    block: SimpleKnotBuilder<State, Event>.() -> Unit
): Knot<State, Event, State, SideEffect<Event>> =
    SimpleKnotBuilderImpl<State, Event>()
    .also(block)
    .build(context, knotState(initialState))

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
 * Creates a [KnotState] instance using a [KnotStateBuilder]
 */
@JvmName("reduxKnotState")
fun <State, Proposal: Any, Model: Any, ReduxModel: Any> reduxKnotState(
    context: KnotContext,
    store: Store<ReduxModel>,
    block: ReduxKnotStateBuilder<State, Model, ReduxModel>.() -> Unit
): KnotState<State, Proposal> =
    ReduxKnotStateBuilderImpl<State, Model, ReduxModel>()
        .also(block)
        .build(context, store)

/**
 * Creates a [KnotState] instance using a [KnotStateBuilder]
 */
@JvmName("simpleReduxKnotState")
fun <State: Any, Proposal: Any, ReduxModel: Any> simpleReduxKnotState(
    context: KnotContext,
    store: Store<ReduxModel>,
    block: ReduxSimpleKnotStateBuilder<State, ReduxModel>.() -> Unit
): KnotState<State, Proposal> =
    ReduxSimpleKnotStateBuilderImpl<State, ReduxModel>()
        .also(block)
        .build(context, store)
