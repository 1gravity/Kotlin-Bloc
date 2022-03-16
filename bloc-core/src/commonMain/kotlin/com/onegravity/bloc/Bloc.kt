package com.onegravity.bloc

import com.onegravity.bloc.state.BlocState

/**
 * TODO implement the orbit demo app in BLoC
 */

interface Bloc<out State, in Action, Proposal> : BlocState<State, Action>