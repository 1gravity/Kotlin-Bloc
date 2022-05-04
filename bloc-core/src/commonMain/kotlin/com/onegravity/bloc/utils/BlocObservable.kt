package com.onegravity.bloc.utils

import com.arkivanov.essenty.lifecycle.Lifecycle

/**
 * A Bloc than can be observed by subscribing to state and side effect updates
 * (has a subscribe function).
 *
 * Note: the subscriptions is tied to the lifecycle of the caller. It depends on the concrete
 * implementations of the BlocObservable which transitions of the lifecycle are relevant (typically
 * onStart() and onStop()).
 */
abstract class BlocObservable<out State: Any, out SideEffect: Any> {

    abstract fun subscribe(
        lifecycle: Lifecycle,
        state: (suspend (state: State) -> Unit)? = null,
        sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
    )

}
