package com.onegravity.knot

import com.onegravity.knot.state.KnotState

interface Knot<out State, in Event, Proposal, SideEffect> : KnotState<State, Event>
