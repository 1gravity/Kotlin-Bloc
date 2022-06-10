package com.onegravity.bloc.redux

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.BlocDSL
import org.reduxkotlin.Store
import kotlin.jvm.JvmName

/**
 * Creates a ReduxBlocState instance using a ReduxBlocStateBuilder.
 *
 * Used internally by the ReduxStore.toBlocState(...) extension function.
 */
@JvmName("reduxBlocState")
@BlocDSL
internal fun <State : Any, Proposal : Any, Model : Any, ReduxModel : Any> reduxBlocState(
    disposableScope: DisposableScope,
    store: Store<ReduxModel>,
    block: ReduxBlocStateBuilder<State, Model, ReduxModel>.() -> Unit
): BlocState<State, Proposal> =
    ReduxBlocStateBuilderImpl<State, Model, ReduxModel>()
        .also(block)
        .build(disposableScope, store)
