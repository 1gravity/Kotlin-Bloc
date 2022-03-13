package com.onegravity.knot

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.disposable.scope.doOnDispose
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.select.select
import com.onegravity.knot.state.reduxKnotState
import com.onegravity.knot.state.simpleReduxKnotState
import org.reduxkotlin.Store

/**
 * Create a DisposableScope that is disposed when the Knot is destroyed (onDestroy called)
 */
fun KnotContext.disposableScope() = DisposableScope()
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
fun <State: Any, SelectedState: Any> DisposableScope.selectScoped(
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
fun <State: Any, SelectedState: Any> Store<State>.selectScoped(
    disposableScope: DisposableScope,
    selector: (State) -> SelectedState,
    block: (selectedState: SelectedState) -> Unit
) {
    val unsubscribe = select(selector) { block(it) }
    disposableScope.doOnDispose { unsubscribe() }
}

/**
 * Extension function to convert a Redux store to a ReduxKnotState:
 * ```
 *    store.toKnotState(context, initialValue) { /* select function */ }
 * ```
 */
fun <State: Any, Proposal: Any, ReduxModel: Any> Store<ReduxModel>.toKnotState(
    context: KnotContext,
    initialState: State,
    selector: Selector<ReduxModel, State>
) = simpleReduxKnotState<State, Proposal, ReduxModel>(context.disposableScope(), this) {
    this.initialState = initialState
    select(selector)
}

/**
 * Extension function to convert a Redux store to a ReduxKnotState:
 * ```
 *    store.toKnotState(context, initialValue, { /* select function */ },  { /* map function */ })
 * ```
 */
fun <State: Any, Proposal: Any, Model: Any, ReduxModel: Any> Store<ReduxModel>.toKnotState(
    context: KnotContext,
    initialState: State,
    selector: Selector<ReduxModel, Model>,
    mapper: Mapper<Model, State>
) = reduxKnotState<State, Proposal, Model, ReduxModel>(context.disposableScope(), this) {
    this.initialState = initialState
    select(selector)
    map(mapper)
}
