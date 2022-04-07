package com.onegravity.bloc.redux

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.BlocDSL
import org.reduxkotlin.Reducer
import org.reduxkotlin.Store
import org.reduxkotlin.createThreadSafeStore
import kotlin.jvm.JvmName

/**
 * Creates a [ReduxBlocState] instance using a [ReduxBlocStateBuilder]
 *
 * Don't call this directly but use the Store extension functions reduxStore.toBlocState(...)
 */
@JvmName("reduxBlocState")
@BlocDSL
internal fun <State, Proposal: Any, Model: Any, ReduxModel: Any> reduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxBlocStateBuilder<State, Model, ReduxModel>.() -> Unit
): BlocState<State, Proposal> =
    ReduxBlocStateBuilderImpl<State, Model, ReduxModel>()
        .also(block)
        .build(disposableScope, store)

/**
 * Creates a [ReduxBlocState] instance using a [ReduxSimpleBlocStateBuilder]
 * (Model == State -> no mapping function).
 * 
 * Don't use this directly but use the Store extension functions reduxStore.toBlocState(...)
 */
@JvmName("simpleReduxBlocState")
@BlocDSL
internal fun <State: Any, Proposal: Any, ReduxModel: Any> reduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxSimpleBlocStateBuilder<State, ReduxModel>.() -> Unit
): BlocState<State, Proposal> =
    ReduxSimpleBlocStateBuilderImpl<State, ReduxModel>()
        .also(block)
        .build(disposableScope, store)

/**
 * Creates a [ReduxBlocState] instance with State == Action meaning there's no further reduction
 * happening. Actions become State right away.
 */
// todo this isn't useful yet, we want to use the Selector to slice and dice the ReduxStore model
//      on the way out AND in, so not just select sub state to stream to the ui but also reduce just
//      the selected part of the full Redux model.
@BlocDSL
inline fun <reified State: Any> BlocContext.reduxBlocState(
    initialState: State
): BlocState<State, State> =
    createThreadSafeStore(
        { state: State, action: Any -> if (action is State) action else state },
        initialState
    ).toBlocState(this, initialState)
