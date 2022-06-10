package com.onegravity.bloc.state

import com.onegravity.bloc.utils.BlocDSL
import kotlin.jvm.JvmName

/**
 * Creates a [BlocState] instance using a [BlocStateBuilder]
 * ```
 *    blocState<State, Proposal> {
 *       initialState = SomeState
 *       accept { proposal, state ->
 *          // map Proposal to State
 *       }
 *    }
 * ```
 */
@JvmName("blocState")
@BlocDSL
public fun <State : Any, Proposal : Any> blocState(
    block: BlocStateBuilder<State, Proposal>.() -> Unit
): BlocState<State, Proposal> =
    BlocStateBuilderImpl<State, Proposal>()
        .also(block)
        .build()

/**
 * Creates a [BlocState] instance using a [SimpleBlocStateBuilder]
 * (Proposal == State -> no accept function needed):
 * ```
 *    blocState<State>(SomeState)
 * ```
 */
@JvmName("blocStateInitialValue")
@BlocDSL
public fun <State : Any> blocState(
    initialState: State
): BlocState<State, State> =
    SimpleBlocStateBuilderImpl<State>()
        .also { it.initialState = initialState }
        .build()
