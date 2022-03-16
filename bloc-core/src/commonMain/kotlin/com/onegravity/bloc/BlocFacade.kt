package com.onegravity.bloc

/**
 * The BlocFacade is the "public" part of a Bloc (and a BlocState).
 * It defines a Stream of 'State' and a Sink of 'Actions' as the part of a Bloc/BlocState that is
 * exposed to the UI and other consuming Blocs.
 */
interface BlocFacade<out State, in Action> : Stream<State>, Sink<Action>
