package com.onegravity.bloc.utils

/**
 * A component that holds a BlocObservable.
 * Using extension functions, users of that component can subscribe to state and side effect updates
 * from the bloc by subscribing to that component:
 * ```
 *   component.subscribe(state = ::render, sideEffect = ::sideEffect)
 * ```
 */
interface BlocObservableOwner<out State, out SideEffect> {

    val observable: BlocObservable<State, SideEffect>

}
