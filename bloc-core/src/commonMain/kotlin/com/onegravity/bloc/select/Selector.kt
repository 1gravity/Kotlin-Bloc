/**
 * Adapted version from: https://github.com/reduxkotlin/Reselect
 */

package com.onegravity.bloc.select

import org.reduxkotlin.*
import kotlin.jvm.JvmField

interface SelectorInput<S, I> {
    operator fun invoke(state: S): I
    val equalityCheck: EqualityCheckFn
}

/**
 * Note: [Selector] inherit from [SelectorInput] because of support for composite selectors
 */
interface Selector<S, O> : SelectorInput<S, O> {
    val recomputations: Long

    fun isChanged(): Boolean

    /**
     * by calling this method, you will force the next call to [getIfChangedIn] to succeed,
     * as if the actual value of the selector was changed, but no actual recomputation is performed
     */
    fun signalChanged()

    fun resetChanged()

    fun getIfChangedIn(state: S): O? {
        val res = invoke(state)
        if (isChanged()) {
            resetChanged()
            return res
        }
        return null
    }

    fun onChangeIn(state: S, blockFn: (O) -> Unit) {
        getIfChangedIn(state)?.let(blockFn)
    }
}

/**
 * Abstract base class for all selectors
 */
abstract class AbstractSelector<S, O> : Selector<S, O> {
    @JvmField
    protected var recomputationsLastChanged = 0L
    @JvmField
    protected var _recomputations = 0L
    override val recomputations: Long get() = _recomputations

    /**
     * see documentation to [Selector.signalChanged]
     */
    override fun signalChanged() {
        ++_recomputations
    }

    override fun isChanged(): Boolean = _recomputations != recomputationsLastChanged

    override fun resetChanged() {
        recomputationsLastChanged = _recomputations
    }

    protected abstract val computeAndCount: (i: Array<out Any>) -> O

    /**
     * 'lazy' because computeAndCount is abstract.
     * Cannot reference to it before it is initialized in concrete selectors 'open' because we can
     * provide a custom memoizer if needed.
     */
    open val memoizer by lazy { computationMemoizer(computeAndCount) }
}

/**
 * A selector function is a function mapping a field in state object to the input for the selector
 * compute function
 */
class InputField<S, I>(
    val fn: S.() -> I,
    override val equalityCheck: EqualityCheckFn
) : SelectorInput<S, I> {
    override operator fun invoke(state: S): I = state.fn()
}

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
fun <State : Any> Store<State>.selectors(
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

fun <State: Any, SelectedState: Any> Store<State>.select(
    selector: (State) -> SelectedState,
    onChange: (SelectedState) -> Unit
) = selectors { select(selector, onChange) }
