package com.onegravity.bloc.state.redux

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.BlocDSL
import org.reduxkotlin.Store
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
