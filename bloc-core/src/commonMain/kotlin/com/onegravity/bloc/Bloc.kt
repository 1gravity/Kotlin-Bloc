package com.onegravity.bloc

import com.onegravity.bloc.state.BlocState

/**
 * The core class of the BLoC framework.
 * (can't be an interface or we won't have the generics in Swift)
 *
 * A Bloc is (as a BlocFacade)
 * - a StateStream<State> emitting state
 * - a Sink<Action> accepting actions that might trigger state changes
 * - a SideEffectStream<SideEffect> emitting side effects that can be used e.g. for navigation
 *
 * A Bloc is also a BlocState since BlocState is a subset of BlocFacade (StateStream and Sink).
 * This allows us to use a Bloc as BlocState and thus create a chain of composable Blocs.
 */
abstract class Bloc<out State: Any, in Action: Any, SideEffect: Any, Proposal: Any> :
    BlocFacade<State, Action, SideEffect>,
    BlocState<State, Action>
