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
fun <State, Action: Any, SideEffect, Proposal> bloc(
    context: BlocContext,
    blocState: BlocState<State, Proposal>,
    block: BlocBuilder<State, Action, SideEffect, Proposal>.() -> Unit
): Bloc<State, Action, SideEffect, Proposal> =
    BlocBuilder<State, Action, SideEffect, Proposal>()
        .also(block)
        .build(context, blocState)

@JvmName("blocInitialValue")
@BlocDSL
fun <State, Action: Any, SideEffect, Proposal> bloc(
    context: BlocContext,
    initialValue: State,
    block: BlocBuilder<State, Action, SideEffect, Proposal>.() -> Unit
): Bloc<State, Action, SideEffect, Proposal> =
    BlocBuilder<State, Action, SideEffect, Proposal>()
        .also(block)
        .build(context, blocState<State, Proposal>{
            initialState = initialValue
        })

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
@JvmName("blocSimplified1")
@BlocDSL
fun <State, Action: Any, Proposal> bloc(
    context: BlocContext,
    blocState: BlocState<State, Proposal>,
    block: BlocBuilder<State, Action, Unit, Proposal>.() -> Unit
): Bloc<State, Action, Unit, Proposal> =
    BlocBuilder<State, Action, Unit, Proposal>()
        .also(block)
        .build(context, blocState)

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
@JvmName("blocSimplified2")
@BlocDSL
fun <State, Action: Any> bloc(
    context: BlocContext,
    blocState: BlocState<State, State>,
    block: BlocBuilder<State, Action, Unit, State>.() -> Unit
): Bloc<State, Action, Unit, State> =
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
@JvmName("blocSimplified3")
@BlocDSL
fun <State, Action: Any> bloc(
    context: BlocContext,
    initialValue: State,
    block: BlocBuilder<State, Action, Unit, State>.() -> Unit
): Bloc<State, Action, Unit, State> =
    BlocBuilder<State, Action, Unit, State>()
        .also(block)
        .build(context, blocState(initialValue))
