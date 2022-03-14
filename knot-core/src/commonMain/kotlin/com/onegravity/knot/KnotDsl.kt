package com.onegravity.knot

import com.onegravity.bloc.Bloc
import com.onegravity.knot.builder.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.BlocState
import com.onegravity.knot.state.knotState
import kotlin.jvm.JvmName

/**
 * Creates a [Bloc] instance using a [FullKnotBuilder].
 */
@JvmName("fullKnot")
fun <State, Event, Proposal, SideEffect> knot(
    context: BlocContext,
    knotState: BlocState<State, Proposal>,
    block: FullKnotBuilder<State, Event, Proposal, SideEffect>.() -> Unit
): Bloc<State, Event, Proposal> =
    FullKnotBuilderImpl<State, Event, Proposal, SideEffect>()
    .also(block)
    .build(context, knotState)

/**
 * Creates a [Bloc] instance using a [SimpleKnotBuilder]
 */
@JvmName("simplerKnot")
fun <State, Event> knot(
    context: BlocContext,
    knotState: BlocState<State, State>,
    block: SimpleKnotBuilder<State, Event>.() -> Unit
): Bloc<State, Event, State> =
    SimpleKnotBuilderImpl<State, Event>()
        .also(block)
        .build(context, knotState)

/**
 * Creates a [Bloc] instance using a [SimpleKnotBuilder].
 */
@JvmName("simplestKnot")
fun <State, Event> knot(
    context: BlocContext,
    initialState: State,
    block: SimpleKnotBuilder<State, Event>.() -> Unit
): Bloc<State, Event, State> =
    SimpleKnotBuilderImpl<State, Event>()
    .also(block)
    .build(context, knotState(initialState))
