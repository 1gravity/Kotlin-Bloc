package com.onegravity.bloc.utils

/**
 * A class is a BlocObservableOwner if it holds and exposes a BlocObservable.
 *
 * Using extension functions, users of that component can subscribe to the bloc's state and side
 * effect updates by subscribing to that component with a one-liner:
 * ```
 *   component.subscribe(state = ::render, sideEffect = ::sideEffect)
 * ```
 *
 * If a component is a BlocOwner it doesn't have to implement BlocObservableOwner since a BlocOwner
 * can also be observed (it has an observable Bloc) and every extension functions for
 * BlocObservableOwner is also implemented for BlocOwner.
 */
interface BlocObservableOwner<out State, out SideEffect> {

    val observable: BlocObservable<State, SideEffect>

}
