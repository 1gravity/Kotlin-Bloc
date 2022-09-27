package com.onegravity.bloc.redux.select

internal interface SelectorInput<S, I> {
    operator fun invoke(state: S): I
    val equalityCheck: EqualityCheckFn
}
