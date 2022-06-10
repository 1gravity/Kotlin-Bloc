package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.BlocObserver
import com.onegravity.bloc.utils.SideEffectStream

/**
 * The core class of the BLoC framework.
 * (can't be an interface or we won't have the generics in Swift)
 *
 * A Bloc is also a BlocState (allowing us to use a Bloc as BlocState and create composable Blocs):
 * - a StateStream<State> emitting state
 * - a Sink<Action> accepting actions that might trigger state changes
 *
 * A Bloc is also
 * - a SideEffectStream<SideEffect> emitting side effects that can be used e.g. for navigation
 */
public abstract class Bloc<out State : Any, in Action : Any, SideEffect : Any> :
    BlocState<State, Action>() {

    public abstract val sideEffects: SideEffectStream<SideEffect>

    /**
     * This observe function is used for iOS to make sure generic types aren't erased.
     * It's the equivalent of the subscribe extension function for Android.
     * Note: the state and sideEffect parameters are of type BlocObserver which is different
     * from the signature used in the Android observe function.
     */
    public abstract fun observe(
        observerLifecycle: Lifecycle,
        state: BlocObserver<State>?,
        sideEffect: BlocObserver<SideEffect>?
    )

}
