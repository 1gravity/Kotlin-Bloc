package com.onegravity.bloc.state.redux

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.disposable.scope.doOnDispose
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.select.select
import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import org.reduxkotlin.Store

/**
 * Create a DisposableScope that is disposed when the Bloc is destroyed (onDestroy called)
 */
@BlocDSL
fun BlocContext.disposableScope() = DisposableScope()
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
fun <State : Any, SelectedState : Any> DisposableScope.selectScoped(
    store: Store<State>,
    selector: (State) -> SelectedState,
    block: (selectedState: SelectedState) -> Unit
) {
    store
        .select(selector) { block(it) }
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
fun <State : Any, SelectedState : Any> Store<State>.selectScoped(
    disposableScope: DisposableScope,
    selector: (State) -> SelectedState,
    block: (selectedState: SelectedState) -> Unit
) {
    val unsubscribe = select(selector) { block(it) }
    disposableScope.doOnDispose { unsubscribe() }
}

/**
 * Extension function to convert a Redux store to a ReduxBlocState:
 * ```
 *    store.toBlocState(context, initialValue)
 * ```
 */
@BlocDSL
fun <State : Any, Proposal : Any> Store<State>.toBlocState(
    context: BlocContext,
    initialState: State,
) = reduxBlocState<State, Proposal, State>(context.disposableScope(), this) {
    this.initialState = initialState
    select { it }
}

/**
 * Extension function to convert a Redux store to a ReduxBlocState:
 * ```
 *    store.toBlocState(context, initialValue) { /* select function */ }
 * ```
 */
@BlocDSL
fun <State : Any, Proposal : Any, ReduxModel : Any> Store<ReduxModel>.toBlocState(
    context: BlocContext,
    initialState: State,
    selector: Selector<ReduxModel, State>
) = reduxBlocState<State, Proposal, ReduxModel>(context.disposableScope(), this) {
    this.initialState = initialState
    select(selector)
}

/**
 * Extension function to convert a Redux store to a ReduxBlocState:
 * ```
 *    store.toBlocState(context, initialValue, { /* select function */ },  { /* map function */ })
 * ```
 */
@BlocDSL
fun <State : Any, Proposal : Any, Model : Any, ReduxModel : Any> Store<ReduxModel>.toBlocState(
    context: BlocContext,
    initialState: State,
    selector: Selector<ReduxModel, Model>,
    mapper: Mapper<Model, State>
) = reduxBlocState<State, Proposal, Model, ReduxModel>(context.disposableScope(), this) {
    this.initialState = initialState
    select(selector)
    map(mapper)
}
