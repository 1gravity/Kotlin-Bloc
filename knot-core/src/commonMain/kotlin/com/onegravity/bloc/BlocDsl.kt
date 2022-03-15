package com.onegravity.bloc

import com.onegravity.bloc.builder.BlocBuilder
import com.onegravity.bloc.context.BlocContext
import com.onegravity.knot.state.blocState
import kotlin.jvm.JvmName

/**
 * Creates a [Bloc] instance using a [BlocBuilder].
 */
@JvmName("blocFull")
fun <State, Action: Any, Proposal> bloc(
    context: BlocContext,
    knotState: BlocState<State, Proposal>,
    block: BlocBuilder<State, Action, Proposal>.() -> Unit
): Bloc<State, Action, Proposal> =
    BlocBuilder<State, Action, Proposal>()
        .also(block)
        .build(context, knotState)

@JvmName("blocSimple")
fun <State, Action: Any> bloc(
    context: BlocContext,
    knotState: BlocState<State, State>,
    block: BlocBuilder<State, Action, State>.() -> Unit
): Bloc<State, Action, State> =
    BlocBuilder<State, Action, State>()
        .also(block)
        .build(context, knotState)

@JvmName("blocSimplest")
fun <State, Action: Any> bloc(
    context: BlocContext,
    initialValue: State,
    block: BlocBuilder<State, Action, State>.() -> Unit
): Bloc<State, Action, State> =
    BlocBuilder<State, Action, State>()
        .also(block)
        .build(context, blocState(initialValue))
