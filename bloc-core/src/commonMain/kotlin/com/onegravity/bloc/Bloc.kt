package com.onegravity.bloc

import com.onegravity.bloc.state.BlocState

/**
 * The core interface of the BLoC framework.
 *
 * The Stream<SideEffect> is a hot stream without replay cache meaning consumers will receive
 * values that are emitted after the subscription (no initial value).
 * in Rx terms Stream<SideEffect> is a PublishSubject and Stream<State> is a BehaviorSubject.
 */
interface Bloc<out State, in Action/*, out SideEffect*/, out Proposal> :
    BlocFacade<State, Action>,
    BlocState<State, Action> {  // this declaration is redundant but let's be explicit

//    val sideEffects: Stream<SideEffect>

}

