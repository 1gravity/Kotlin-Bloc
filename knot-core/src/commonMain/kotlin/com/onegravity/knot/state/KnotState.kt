package com.onegravity.knot.state

import com.onegravity.knot.KnotFacade

interface KnotState<out State, in Proposal> : KnotFacade<State, Proposal>
