package com.onegravity.bloc

import com.onegravity.bloc.builder.BlocBuilderImpl
import com.onegravity.bloc.context.BlocContext
import com.onegravity.knot.state.knotState
import kotlin.jvm.JvmName

/**
 * Creates a [Bloc] instance using a [BlocBuilderImpl].
 */
@JvmName("blocFull")
fun <State, Action, Proposal> bloc(
    context: BlocContext,
    knotState: BlocState<State, Proposal>,
    block: BlocBuilderImpl<State, Action, Proposal>.() -> Unit
): Bloc<State, Action, Proposal> =
    BlocBuilderImpl<State, Action, Proposal>()
        .also(block)
        .build(context, knotState)

@JvmName("blocSimple")
fun <State, Action> bloc(
    context: BlocContext,
    knotState: BlocState<State, State>,
    block: BlocBuilderImpl<State, Action, State>.() -> Unit
): Bloc<State, Action, State> =
    BlocBuilderImpl<State, Action, State>()
        .also(block)
        .build(context, knotState)

@JvmName("blocSimplest")
fun <State, Action> bloc(
    context: BlocContext,
    initialValue: State,
    block: BlocBuilderImpl<State, Action, State>.() -> Unit
): Bloc<State, Action, State> =
    BlocBuilderImpl<State, Action, State>()
        .also(block)
        .build(context, knotState(initialValue))
