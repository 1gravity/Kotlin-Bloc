package com.onegravity.bloc.state

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.state.redux.ReduxBlocState
import com.onegravity.bloc.state.redux.ReduxBlocStateBuilder
import com.onegravity.bloc.state.redux.ReduxBlocStateBuilderImpl
import com.onegravity.bloc.state.redux.ReduxSimpleBlocStateBuilder
import com.onegravity.bloc.utils.BlocDSL
import org.reduxkotlin.Store
import kotlin.jvm.JvmName

/**
 * Creates a [BlocState] instance using a [BlocStateBuilder]
 * ```
 *    blocState<State, Proposal> {
 *       initialState = SomeState
 *       accept { proposal, state ->
 *          // map proposal to State
 *       }
 *    }
 * ```
 */
@JvmName("blocState")
@BlocDSL
fun <State, Proposal> blocState(
    block: BlocStateBuilder<State, Proposal>.() -> Unit
): BlocState<State, Proposal> =
    BlocStateBuilderImpl<State, Proposal>()
        .also(block)
        .build()

/**
 * Creates a [BlocState] instance using a [SimpleBlocStateBuilder]:
 * ```
 *    blocState<State> { initialState = SomeState }
 * ```
 */
@JvmName("simpleBlocState")
@BlocDSL
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
@BlocDSL
fun <State> blocState(
    initialState: State
): BlocState<State, State> =
    SimpleBlocStateBuilderImpl<State>()
        .also { it.initialState = initialState }
        .build()

/**
 * Creates a [BlocState] instance using a [ReduxBlocStateBuilder]
 *
 * Note: don't use this directly but use the Store extension functions
 * reduxStore.toBlocState(...)
 */
@JvmName("reduxBlocState")
@BlocDSL
fun <State, Proposal: Any, Model: Any, ReduxModel: Any> reduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxBlocStateBuilder<State, Model, ReduxModel>.() -> Unit
): ReduxBlocState<State, Proposal, Model, ReduxModel> =
    ReduxBlocStateBuilderImpl<State, Model, ReduxModel>()
        .also(block)
        .build(disposableScope, store)

/**
 * Creates a [BlocState] instance using a [ReduxSimpleBlocStateBuilder]
 * (Model == State -> no mapping function).
 * 
 * Note: don't use this directly but use the Store extension functions
 * reduxStore.toBlocState(...)
 */
@JvmName("simpleReduxBlocState")
@BlocDSL
fun <State: Any, Proposal: Any, ReduxModel: Any> reduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxSimpleBlocStateBuilder<State, ReduxModel>.() -> Unit
): ReduxBlocState<State, Proposal, State, ReduxModel> =
    ReduxSimpleBlocStateBuilderImpl<State, ReduxModel>()
        .also(block)
        .build(disposableScope, store)
