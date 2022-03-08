package com.onegravity.knot

import com.onegravity.knot.state.KnotState
import kotlinx.coroutines.CoroutineScope

/**
 * TODO move all knot code into a shared module
 * TODO rename everything to BLoC
 * TODO implement the orbit demo app in BLoC
 */

interface Knot<out State, in Event, Proposal, SideEffect> : KnotState<State, Event> {

    fun start(coroutineScope: CoroutineScope)

    fun stop()

}
