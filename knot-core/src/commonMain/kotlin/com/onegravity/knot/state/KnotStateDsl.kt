package com.onegravity.knot.state

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.BlocState
import org.reduxkotlin.Store
import kotlin.jvm.JvmName

/**
 * Creates a [BlocState] instance using a [SimpleKnotStateBuilder]
 */
@JvmName("simpleKnotState")
fun <State> blocState(
    block: SimpleKnotStateBuilder<State>.() -> Unit
): BlocState<State, State> =
    SimpleKnotStateBuilderImpl<State>()
        .also(block)
        .build()

/**
 * Similar to the previous [blocState] function but takes the initialState as function parameter to
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
fun <State> blocState(
    initialState: State
): BlocState<State, State> =
    SimpleKnotStateBuilderImpl<State>()
        .also { it.initialState = initialState }
        .build()

/**
 * Creates a [BlocState] instance using a [KnotStateBuilder]
 */
@JvmName("knotState")
fun <State, Proposal> blocState(
    block: KnotStateBuilder<State, Proposal>.() -> Unit
): BlocState<State, Proposal> =
    KnotStateBuilderImpl<State, Proposal>()
        .also(block)
        .build()

/**
 * Creates a [BlocState] instance using a [ReduxKnotStateBuilder]
 */
@JvmName("reduxKnotState")
fun <State, Proposal: Any, Model: Any, ReduxModel: Any> reduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxKnotStateBuilder<State, Model, ReduxModel>.() -> Unit
): BlocState<State, Proposal> =
    ReduxKnotStateBuilderImpl<State, Model, ReduxModel>()
        .also(block)
        .build(disposableScope, store)

/**
 * Creates a [BlocState] instance using a [ReduxSimpleKnotStateBuilder]
 */
@JvmName("simpleReduxKnotState")
fun <State: Any, Proposal: Any, ReduxModel: Any> simpleReduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxSimpleKnotStateBuilder<State, ReduxModel>.() -> Unit
): BlocState<State, Proposal> =
    ReduxSimpleKnotStateBuilderImpl<State, ReduxModel>()
        .also(block)
        .build(disposableScope, store)
