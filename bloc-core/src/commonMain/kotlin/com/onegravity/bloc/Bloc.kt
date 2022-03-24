package com.onegravity.bloc

import com.onegravity.bloc.context.BlocContextOwner
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.Stream

/**
 * The core interface of the BLoC framework.
 *
 * The Stream<SideEffect> is a hot stream without replay cache meaning consumers will receive
 * values that are emitted after the subscription (no initial value).
 * in Rx terms Stream<SideEffect> is a PublishSubject and Stream<State> is a BehaviorSubject.
 */
interface Bloc<out State, in Action, SideEffect, Proposal> :
    BlocFacade<State, Action>,
    // the following declaration is redundant (a BlocState is a BlocFacade) but let's be explicit
    BlocState<State, Action>,
    BlocContextOwner {

    val sideEffects: Stream<SideEffect>

}

