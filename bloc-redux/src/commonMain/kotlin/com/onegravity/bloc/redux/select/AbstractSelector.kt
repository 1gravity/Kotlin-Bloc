package com.onegravity.bloc.redux.select

import kotlin.jvm.JvmField

/**
 * Abstract base class for all selectors
 */
internal abstract class AbstractSelector<S, O> : Selector<S, O> {
    @JvmField
    protected var recomputationsLastChanged = 0L

    @JvmField
    protected var _recomputations = 0L

    override val recomputations: Long
        get() = _recomputations

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
