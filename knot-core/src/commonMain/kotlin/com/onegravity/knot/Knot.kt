package com.onegravity.knot

import com.onegravity.knot.state.KnotState

/**
 * TODO rename everything to BLoC
 * TODO implement the orbit demo app in BLoC
 */

interface Knot<out State, in Event, Proposal, SideEffect> : KnotState<State, Event>