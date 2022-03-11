package com.onegravity.knot

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.select.select
import org.reduxkotlin.Store

/**
 * Create a DisposableScope that is disposed when the KnotContext lifecycle ends (onDestroy called)
 */
fun KnotContext.disposableScope() = DisposableScope()
    .apply { lifecycle.doOnDestroy(::dispose) }

/**
 * Add a state selection subscription to a DisposableScope so it will be properly disposed when
 * the DisposableScope is disposed.
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
