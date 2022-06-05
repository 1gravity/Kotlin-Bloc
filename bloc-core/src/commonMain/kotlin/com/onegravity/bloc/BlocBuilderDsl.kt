package com.onegravity.bloc

import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.utils.BlocDSL
import kotlin.jvm.JvmName

/**
 * Creates a [Bloc] instance using a [BlocBuilder].
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

@JvmName("blocInitialValue")
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> bloc(
    context: BlocContext,
    initialValue: State,
    block: BlocBuilder<State, Action, SideEffect, Proposal>.() -> Unit = {}
): Bloc<State, Action, SideEffect> =
    BlocBuilder<State, Action, SideEffect, Proposal>()
        .also(block)
        .build(context, blocState { initialState = initialValue })

/**
 * Creates a [Bloc] instance using a [BlocBuilder].
 *
 * ```
 * bloc<State, Action, SideEffect>(context, blocState) {
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
 * Creates a [Bloc] instance using a [BlocBuilder].
 *
 * ```
 * bloc<State, Action, SideEffect>(context, initialValue) {
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
 * Creates a [Bloc] instance using a [BlocBuilder] with Proposal being State
 * (the [BlocState] doesn't do any extra mapping from Proposal to State).
 *
 * ```
 * bloc<State, Action>(context, blocState) {
 *    thunk { getState, action, dispatch ->
 *       ...Uni
 *    }
 *    reduce { state, action ->
 *       ...
 *    }
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
 * Creates a [Bloc] instance using a [BlocBuilder] with Proposal being State and an initial value
 * (no need to create a separate [BlocState].
 *
 * ```
 * bloc<State, Action>(context, initialValue) {
 *    thunk { getState, action, dispatch ->
 *       ...
 *    }
 *    reduce { state, action ->
 *       ...
 *    }
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
