package com.onegravity.bloc

import com.onegravity.bloc.utils.Sink
import com.onegravity.bloc.utils.StateStream

/**
 * The BlocFacade is the "public" part of a Bloc (and a BlocState).
 * It defines a Stream of 'State' and a Sink of 'Actions' as the part of a Bloc/BlocState that is
 * exposed to the UI and other consuming Blocs.
 *
 * The Stream<State> is a hot stream with a replay cache of 1 which means consumers will receive
 * the latest value upon subscription.
 */
interface BlocFacade<out State, in Action> : StateStream<State>, Sink<Action>
