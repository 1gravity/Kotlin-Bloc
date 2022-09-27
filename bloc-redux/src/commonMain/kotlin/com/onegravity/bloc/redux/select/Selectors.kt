/**
 * Adapted version from: https://github.com/reduxkotlin/Reselect
 */

package com.onegravity.bloc.redux.select

import org.reduxkotlin.Store
import org.reduxkotlin.StoreSubscriber

/**
 * Helper function that creates a DSL for subscribing to changes in specific state fields and
 * actions to take.
 * Inside the lambda there is access to the selected state through the var `it`
 *
 * ex:
 *      val sel = selectorSubscriberFn {
 *          select({it.foo}, { foo -> actionWhenFooChanges() }
 *
 *          withAnyChange {
 *              //called whenever any change happens to state
 *              view.setMessage(state.barMsg) //state is current state
 *          }
 *      }
 */
internal fun <State : Any> Store<State>.selectors(
    subscriberBuilderBlock: SelectorSubscriberBuilder<State>.() -> Unit
): StoreSubscriber {
    val subscriberBuilder: SelectorSubscriberBuilder<State> = SelectorSubscriberBuilder(this)
    subscriberBuilder.subscriberBuilderBlock()
    val sub = {
        subscriberBuilder.selectorList.forEach { (selector, action) ->
            selector.onChangeIn(getState()) {
                val filter = subscriberBuilder.selectorList2[selector]
                val state = filter?.invoke(getState())!!
                action(state)
            }
        }
        subscriberBuilder.withAnyChangeFun?.invoke()
        Unit
    }
    // call subscriber immediately when subscribing
    sub()
    return this.subscribe(sub)
}

public fun <State : Any, SelectedState : Any> Store<State>.select(
    selector: (State) -> SelectedState,
    onChange: (SelectedState) -> Unit
): StoreSubscriber = selectors { select(selector, onChange) }
