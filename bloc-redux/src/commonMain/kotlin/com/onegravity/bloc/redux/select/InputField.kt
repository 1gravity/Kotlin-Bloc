package com.onegravity.bloc.redux.select

/**
 * A selector function is a function mapping a field in state object to the input for the selector
 * compute function
 */
internal class InputField<S, I>(
    val fn: S.() -> I,
    override val equalityCheck: EqualityCheckFn
) : SelectorInput<S, I> {
    override operator fun invoke(state: S): I = state.fn()
}
