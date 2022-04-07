/**
 * Adapter version from: https://github.com/reduxkotlin/Reselect
 */

package com.onegravity.bloc.redux.select

import org.reduxkotlin.Store

/**
 * A Selector Subscriber - group of selectors that subscribe to store state changes.
 *
 * @param State is the type of the state object returned by the store.
 * @param SelectedState is the type of the state object returned by the selector function.
 * @property store The redux store
 * @constructor creates an empty SelectorSubscriberBuilder
 */
class SelectorSubscriberBuilder<State : Any>(val store: Store<State>) {

    val selectorList = mutableMapOf<Selector<State, Any>, (Any) -> Unit>()
    val selectorList2 = mutableMapOf<Selector<State, Any>, (State) -> Any>()

    // state is here to make available to lambda with receiver in DSL
    val state: State
        get() = store.getState()

    var withAnyChangeFun: (() -> Unit)? = null

    fun withAnyChange(f: () -> Unit) {
        withAnyChangeFun = f
    }

    @Suppress("UNCHECKED_CAST")
    fun <SelectedState: Any> select(selector: (State) -> SelectedState, action: (SelectedState) -> Unit) {
        val selBuilder = SelectorBuilder<State>()
        val sel = selBuilder.withSingleField(selector) as Selector<State, Any>
        selectorList[sel] = action as (Any) -> Unit
        selectorList2[sel] = selector
    }
}
