package com.onegravity.bloc

import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.SideEffectStream
import com.onegravity.bloc.utils.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

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
abstract class Bloc<out State : Any, in Action : Any, SideEffect : Any> :
    BlocState<State, Action>() {

    abstract val sideEffects: SideEffectStream<SideEffect>

}
