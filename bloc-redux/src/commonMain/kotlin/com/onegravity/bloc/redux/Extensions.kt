package com.onegravity.bloc.redux

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.disposable.scope.doOnDispose
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.redux.select.select
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import org.reduxkotlin.Store

/**
 * Extension function to convert a Redux store to a ReduxBlocState:
 * ```
 *    store.toBlocState(context, { /* select function */ },  { /* map function */ })
 * ```
 */
public fun <State : Any, Proposal : Any, Model : Any, ReduxModel : Any> Store<ReduxModel>.toBlocState(
    context: BlocContext,
    select: Selector<ReduxModel, Model>,
    map: Mapper<Model, State>
): BlocState<State, Proposal> = reduxBlocState<State, Proposal, Model, ReduxModel>(context.disposableScope(), this) {
    this.select(select)
    this.map(map)
}

/**
 * Create a DisposableScope that is disposed when the Bloc is destroyed (onDestroy called)
 */
@BlocDSL
internal fun BlocContext.disposableScope() = DisposableScope()
    .apply { lifecycle.doOnDestroy(::dispose) }

/**
 * Add a state selection subscription to a DisposableScope so it will be properly disposed when
 * the DisposableScope is disposed. Call this as:
 * ```
 *    selectScoped(store, selector) { model ->
 *       // do stuff with the selected model
 *    }
 * ```
 */
@BlocDSL
internal fun <State : Any, SelectedState : Any> DisposableScope.selectScoped(
    store: Store<State>,
    select: (State) -> SelectedState,
    block: (selectedState: SelectedState) -> Unit
) {
    store
        .select(select) { block(it) }
        .scope { unsubscribe -> unsubscribe() }
}

/**
 * Add a state selection subscription to a DisposableScope so it will be properly disposed when
 * the DisposableScope is disposed. Call this as:
 * ```
 *    store.selectScoped(disposableScope, selector) { model ->
 *       // do stuff with the selected model
 *    }
 * ```
 */
@BlocDSL
internal fun <State : Any, SelectedState : Any> Store<State>.selectScoped(
    disposableScope: DisposableScope,
    select: (State) -> SelectedState,
    block: (selectedState: SelectedState) -> Unit
) {
    val unsubscribe = select(select) { block(it) }
    disposableScope.doOnDispose { unsubscribe() }
}
