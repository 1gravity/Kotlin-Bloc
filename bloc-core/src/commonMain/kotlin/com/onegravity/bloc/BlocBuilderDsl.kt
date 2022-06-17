package com.onegravity.bloc

import com.onegravity.bloc.internal.builder.BlocBuilder
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.utils.BlocDSL
import kotlin.jvm.JvmName

/**
 * Creates a Bloc instance using a BlocBuilder.
 *
 * ```
 * bloc<State, Action, SideEffect, Proposal>(context, blocState) {
 *    thunk { getState, action, dispatch ->
 *       ...
 *    }
 *    sideEffect { state, action ->
 *       ...
 *    }
 *    reduce { state, action ->
 *       ...
 *    }
 * }
 * ```
 */
@JvmName("bloc")
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> bloc(
    context: BlocContext,
    blocState: BlocState<State, Proposal>,
    block: BlocBuilder<State, Action, SideEffect, Proposal>.() -> Unit = {}
): Bloc<State, Action, SideEffect> =
    BlocBuilder<State, Action, SideEffect, Proposal>()
        .also(block)
        .build(context, blocState)

/**
 * Creates a Bloc instance using a BlocBuilder.
 * - Proposal == State
 *
 * ```
 * bloc<State, Action, SideEffect>(context, blocState) {
 *    ...
 * }
 * ```
 */
@JvmName("blocProposalEqualsState")
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any> bloc(
    context: BlocContext,
    blocState: BlocState<State, State>,
    block: BlocBuilder<State, Action, SideEffect, State>.() -> Unit = {}
): Bloc<State, Action, SideEffect> =
    BlocBuilder<State, Action, SideEffect, State>()
        .also(block)
        .build(context, blocState)

/**
 * Creates a Bloc instance using a BlocBuilder.
 * - Proposal == State
 * - initialValue instead of BlocState as argument
 *
 * ```
 * bloc<State, Action, SideEffect>(context, initialValue) {
 *    ...
 * }
 * ```
 */
@JvmName("blocProposalEqualsStateInitialValue")
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any> bloc(
    context: BlocContext,
    initialValue: State,
    block: BlocBuilder<State, Action, SideEffect, State>.() -> Unit = {}
): Bloc<State, Action, SideEffect> =
    BlocBuilder<State, Action, SideEffect, State>()
        .also(block)
        .build(context, blocState(initialValue))

/**
 * Creates a Bloc instance using a BlocBuilder.
 * - Proposal == State
 * - no side effects
 *
 * ```
 * bloc<State, Action>(context, blocState) {
 *    ...
 * }
 * ```
 */
@JvmName("blocNoSideEffects")
@BlocDSL
public fun <State : Any, Action : Any> bloc(
    context: BlocContext,
    blocState: BlocState<State, State>,
    block: BlocBuilder<State, Action, Unit, State>.() -> Unit = {}
): Bloc<State, Action, Unit> =
    BlocBuilder<State, Action, Unit, State>()
        .also(block)
        .build(context, blocState)

/**
 * Creates a Bloc instance using a BlocBuilder.
 * - initialValue instead of BlocState as argument
 * - Proposal == State
 * - no side effects
 *
 * ```
 * bloc<State, Action>(context, blocState) {
 *    ...
 * }
 * ```
 */
@JvmName("blocNoSideEffectsInitialValue")
@BlocDSL
public fun <State : Any, Action : Any> bloc(
    context: BlocContext,
    initialValue: State,
    block: BlocBuilder<State, Action, Unit, State>.() -> Unit = {}
): Bloc<State, Action, Unit> =
    BlocBuilder<State, Action, Unit, State>()
        .also(block)
        .build(context, blocState(initialValue))
