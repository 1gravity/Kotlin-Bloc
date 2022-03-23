package com.onegravity.bloc.utils

/**
 * A Bloc than can be observed by subscribing to state and side effect updates
 * (has a subscribe function).
 */
interface BlocObservable<out State, out SideEffect> {

    fun subscribe(
        state: (suspend (state: State) -> Unit)? = null,
        sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
    )

}
