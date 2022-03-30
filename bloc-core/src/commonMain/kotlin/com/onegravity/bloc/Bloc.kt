package com.onegravity.bloc

/**
 * The core interface of the BLoC framework.
 *
 * A Bloc is (as a BlocFacade)
 * - a StateStream<State> emitting state
 * - a Sink<Action> accepting actions that might trigger state changes
 * - a SideEffectStream<SideEffect> emitting side effects that can be used e.g. for navigation
 *
 * A Bloc is also (almost) a BlocState since BlocState is an subset of BlocFacade (StateStream and
 * Sink but no SideEffectStream). This allows us to use a Bloc as BlocState and thus create a chain
 * of composable Blocs. The "almost" comes down to the fact that a BlocState is also an
 * InstanceKeeper.Instance which allows us to retain it across ui configuration changes.
 */
interface Bloc<out State, in Action, SideEffect, Proposal> :
    BlocFacade<State, Action, SideEffect>
