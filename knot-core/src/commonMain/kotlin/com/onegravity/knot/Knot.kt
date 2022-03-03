package com.onegravity.knot

import com.onegravity.knot.state.KnotState

interface Knot<out State, in Action, Model, SideEffect> : KnotState<State, Action>
