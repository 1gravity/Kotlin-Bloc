package com.onegravity.bloc.redux.select

import kotlin.jvm.JvmField

/**
 * Abstract base class for all selectors
 */
internal abstract class AbstractSelector<S, O> : Selector<S, O> {
    @JvmField
    protected var recomputationsLastChanged = 0L

    @JvmField
    protected var recomputationsBackingField = 0L

    override val recomputations: Long
        get() = recomputationsBackingField

    /**
     * see documentation to [Selector.signalChanged]
     */
    override fun signalChanged() {
        ++recomputationsBackingField
    }

    override fun isChanged(): Boolean = recomputationsBackingField != recomputationsLastChanged

    override fun resetChanged() {
        recomputationsLastChanged = recomputationsBackingField
    }

    protected abstract val computeAndCount: (i: Array<out Any>) -> O

    /**
     * 'lazy' because computeAndCount is abstract.
     * Cannot reference to it before it is initialized in concrete selectors 'open' because we can
     * provide a custom memoizer if needed.
     */
    open val memoizer by lazy { computationMemoizer(computeAndCount) }
}
