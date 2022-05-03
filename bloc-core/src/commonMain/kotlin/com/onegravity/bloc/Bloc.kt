package com.onegravity.bloc

import com.onegravity.bloc.state.BlocState

/**
 * The core interface of the BLoC framework.
 *
 * A Bloc is (as a BlocFacade)
 * - a StateStream<State> emitting state
 * - a Sink<Action> accepting actions that might trigger state changes
 * - a SideEffectStream<SideEffect> emitting side effects that can be used e.g. for navigation
 *
 * A Bloc is also a BlocState since BlocState is a subset of BlocFacade (StateStream and Sink).
 * This allows us to use a Bloc as BlocState and thus create a chain of composable Blocs.
 */
interface Bloc<out State, in Action, SideEffect, Proposal> :
    BlocFacade<State, Action, SideEffect>,
    BlocState<State, Action>
