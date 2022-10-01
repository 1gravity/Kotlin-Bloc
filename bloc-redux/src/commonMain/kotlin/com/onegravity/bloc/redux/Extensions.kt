package com.onegravity.bloc.redux

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.disposable.scope.doOnDispose
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.redux.select.select
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import org.reduxkotlin.Store

/**
 * Extension function to convert a Redux store to a [BlocState].
 *
 * @param select memoized selector function to select sub-state from the (global) redux state
 * @param map maps the selected sub state to bloc state if needed (can be the identity function)
 *
 * @return a [BlocState] with state reflecting the selected and mapped redux store state:
 *         `state = map(select(redux model))`
 *
 * Example:
 * ```
 * store.toBlocState(
 *   context = context,
 *   select = { reduxModel -> reduxModel.books },
 *   map = { model ->
 *     when {
 *       model.isLoading -> BookState.Loading
 *       else -> model.books.toState()
 *     }
 *   }
 * )
 * ```
 */
public fun <State : Any, Proposal : Any, Model : Any, ReduxModel : Any> Store<ReduxModel>.toBlocState(
    context: BlocContext,
    select: Selector<ReduxModel, Model>,
    map: Mapper<Model, State>
): BlocState<State, Proposal> = reduxBlocState(
    disposableScope = context.disposableScope(),
    store = this
) {
    select(select)
    map(map)
}

/**
 * Create a DisposableScope that is disposed when the Bloc is destroyed (onDestroy called)
 */
private fun BlocContext.disposableScope() = DisposableScope()
    .apply { lifecycle.doOnDestroy(::dispose) }

/**
 * Add a state selection subscription to a DisposableScope so it will be properly disposed when
 * the DisposableScope is disposed.
 * ```
 *    selectScoped(store, selector) { model ->
 *       // do stuff with the selected model
 *    }
 * ```
 */
@Suppress("unused")
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
 * the DisposableScope is disposed.
 * ```
 *    store.selectScoped(disposableScope, selector) { model ->
 *       // do stuff with the selected model
 *    }
 * ```
 */
internal fun <State : Any, SelectedState : Any> Store<State>.selectScoped(
    disposableScope: DisposableScope,
    select: (State) -> SelectedState,
    block: (selectedState: SelectedState) -> Unit
) {
    val unsubscribe = select(select) { block(it) }
    disposableScope.doOnDispose { unsubscribe() }
}
