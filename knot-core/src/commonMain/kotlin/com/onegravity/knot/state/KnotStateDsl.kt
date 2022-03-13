package com.onegravity.knot.state

import com.badoo.reaktive.disposable.scope.DisposableScope
import org.reduxkotlin.Store
import kotlin.jvm.JvmName

/**
 * Creates a [KnotState] instance using a [SimpleKnotStateBuilder]
 */
@JvmName("simpleKnotState")
fun <State> knotState(
    block: SimpleKnotStateBuilder<State>.() -> Unit
): KnotState<State, State> =
    SimpleKnotStateBuilderImpl<State>()
        .also(block)
        .build()

/**
 * Similar to the previous [knotState] function but takes the initialState as function parameter to
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
fun <State> knotState(
    initialState: State
): KnotState<State, State> =
    SimpleKnotStateBuilderImpl<State>()
        .also { it.initialState = initialState }
        .build()

/**
 * Creates a [KnotState] instance using a [KnotStateBuilder]
 */
@JvmName("knotState")
fun <State, Proposal> knotState(
    block: KnotStateBuilder<State, Proposal>.() -> Unit
): KnotState<State, Proposal> =
    KnotStateBuilderImpl<State, Proposal>()
        .also(block)
        .build()

/**
 * Creates a [KnotState] instance using a [ReduxKnotStateBuilder]
 */
@JvmName("reduxKnotState")
fun <State, Proposal: Any, Model: Any, ReduxModel: Any> reduxKnotState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxKnotStateBuilder<State, Model, ReduxModel>.() -> Unit
): KnotState<State, Proposal> =
    ReduxKnotStateBuilderImpl<State, Model, ReduxModel>()
        .also(block)
        .build(disposableScope, store)

/**
 * Creates a [KnotState] instance using a [ReduxSimpleKnotStateBuilder]
 */
@JvmName("simpleReduxKnotState")
fun <State: Any, Proposal: Any, ReduxModel: Any> simpleReduxKnotState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxSimpleKnotStateBuilder<State, ReduxModel>.() -> Unit
): KnotState<State, Proposal> =
    ReduxSimpleKnotStateBuilderImpl<State, ReduxModel>()
        .also(block)
        .build(disposableScope, store)
