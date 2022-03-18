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
 * bloc<State, Action, Proposal>(context, blocState) {
 *    thunk { getState, action, dispatch ->
 *       ...
 *    }
 *    reduce { state, action ->
 *       ...
 *    }
 * }
 * ```
 */
@JvmName("blocFull")
@BlocDSL
fun <State, Action: Any, Proposal> bloc(
    context: BlocContext,
    blocState: BlocState<State, Proposal>,
    block: BlocBuilder<State, Action, Proposal>.() -> Unit
): Bloc<State, Action, Proposal> =
    BlocBuilder<State, Action, Proposal>()
        .also(block)
        .build(context, blocState)

/**
 * Creates a [Bloc] instance using a [BlocBuilder] with Proposal being State
 * (the [BlocState] doesn't do any extra mapping from Proposal to State).
 *
 * ```
 * bloc<State, Action>(context, blocState) {
 *    thunk { getState, action, dispatch ->
 *       ...
 *    }
 *    reduce { state, action ->
 *       ...
 *    }
 * }
 * ```
 */
@JvmName("blocSimple")
@BlocDSL
fun <State, Action: Any> bloc(
    context: BlocContext,
    blocState: BlocState<State, State>,
    block: BlocBuilder<State, Action, State>.() -> Unit
): Bloc<State, Action, State> =
    BlocBuilder<State, Action, State>()
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
@JvmName("blocSimplest")
@BlocDSL
fun <State, Action: Any> bloc(
    context: BlocContext,
    initialValue: State,
    block: BlocBuilder<State, Action, State>.() -> Unit
): Bloc<State, Action, State> =
    BlocBuilder<State, Action, State>()
        .also(block)
        .build(context, blocState(initialValue))
