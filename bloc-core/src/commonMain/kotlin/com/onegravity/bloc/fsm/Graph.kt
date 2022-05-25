/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.fsm

internal data class Graph<STATE : Any, EVENT : Any, SIDE_EFFECT : Any>(
    val initialState: STATE,
    val stateDefinitions: Map<Matcher<STATE, STATE>, State<STATE, EVENT, SIDE_EFFECT>>,
    val onTransitionListeners: List<(Transition<STATE, EVENT, SIDE_EFFECT>) -> Unit>
) {

    internal class State<STATE : Any, EVENT : Any, SIDE_EFFECT : Any> internal constructor() {
        internal val onEnterListeners = mutableListOf<(STATE, EVENT) -> Unit>()
        internal val onExitListeners = mutableListOf<(STATE, EVENT) -> Unit>()
        internal val transitions = linkedMapOf<Matcher<EVENT, EVENT>, (STATE, EVENT) -> TransitionTo<STATE, SIDE_EFFECT>>()

        internal data class TransitionTo<out STATE : Any, out SIDE_EFFECT : Any> internal constructor(
            val toState: STATE,
            val sideEffect: SIDE_EFFECT?
        )
    }
}
