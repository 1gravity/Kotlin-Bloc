package com.onegravity.bloc.redux.select

/**
 * Note: [Selector] inherit from [SelectorInput] because of support for composite selectors
 */
internal interface Selector<S, O> : SelectorInput<S, O> {
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
