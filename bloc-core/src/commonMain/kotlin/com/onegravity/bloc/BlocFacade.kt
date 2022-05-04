package com.onegravity.bloc

import com.onegravity.bloc.utils.SideEffectStream
import com.onegravity.bloc.utils.Sink
import com.onegravity.bloc.utils.StateStream

/**
 * The BlocFacade is the "public" part of a Bloc.
 * It defines:
 * - a StateStream<State> emitting state
 * - a Sink<Action> accepting actions that might trigger state changes
 * - a SideEffectStream<SideEffect> emitting side effects that can be used e.g. for navigation
 */
interface BlocFacade<out State: Any, in Action: Any, out SideEffect: Any> : StateStream<State>, Sink<Action> {

    val sideEffects: SideEffectStream<SideEffect>

}
