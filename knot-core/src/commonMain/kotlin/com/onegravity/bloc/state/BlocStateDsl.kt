package com.onegravity.bloc.state

import com.badoo.reaktive.disposable.scope.DisposableScope
import org.reduxkotlin.Store
import kotlin.jvm.JvmName

/**
 * Creates a [BlocState] instance using a [SimpleBlocStateBuilder]
 */
@JvmName("simpleBlocState")
fun <State> blocState(
    block: SimpleBlocStateBuilder<State>.() -> Unit
): BlocState<State, State> =
    SimpleBlocStateBuilderImpl<State>()
        .also(block)
        .build()

/**
 * Similar to the previous [blocState] function but takes the initialState as function parameter to
 * simplify from
 * ```
 *    blocState<State> { initialState = SomeState }
 * ```
 * to
 * ```
 *    blocState<State>(SomeState)
 * ```
 */
@JvmName("simpleBlocStateWithInitialState")
fun <State> blocState(
    initialState: State
): BlocState<State, State> =
    SimpleBlocStateBuilderImpl<State>()
        .also { it.initialState = initialState }
        .build()

/**
 * Creates a [BlocState] instance using a [BlocStateBuilder]
 */
@JvmName("blocState")
fun <State, Proposal> blocState(
    block: BlocStateBuilder<State, Proposal>.() -> Unit
): BlocState<State, Proposal> =
    BlocStateBuilderImpl<State, Proposal>()
        .also(block)
        .build()

/**
 * Creates a [BlocState] instance using a [ReduxBlocStateBuilder]
 */
@JvmName("reduxBlocState")
fun <State, Proposal: Any, Model: Any, ReduxModel: Any> reduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxBlocStateBuilder<State, Model, ReduxModel>.() -> Unit
): BlocState<State, Proposal> =
    ReduxBlocStateBuilderImpl<State, Model, ReduxModel>()
        .also(block)
        .build(disposableScope, store)

/**
 * Creates a [BlocState] instance using a [ReduxSimpleBlocStateBuilder]
 */
@JvmName("simpleReduxBlocState")
fun <State: Any, Proposal: Any, ReduxModel: Any> simpleReduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxSimpleBlocStateBuilder<State, ReduxModel>.() -> Unit
): BlocState<State, Proposal> =
    ReduxSimpleBlocStateBuilderImpl<State, ReduxModel>()
        .also(block)
        .build(disposableScope, store)
