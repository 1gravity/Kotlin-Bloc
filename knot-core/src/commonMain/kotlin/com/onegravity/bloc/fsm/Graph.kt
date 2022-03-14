package com.onegravity.bloc.fsm

data class Graph<STATE : Any, EVENT : Any, SIDE_EFFECT : Any>(
    val initialState: STATE,
    val stateDefinitions: Map<Matcher<STATE, STATE>, State<STATE, EVENT, SIDE_EFFECT>>,
    val onTransitionListeners: List<(Transition<STATE, EVENT, SIDE_EFFECT>) -> Unit>
) {

    class State<STATE : Any, EVENT : Any, SIDE_EFFECT : Any> internal constructor() {
        val onEnterListeners = mutableListOf<(STATE, EVENT) -> Unit>()
        val onExitListeners = mutableListOf<(STATE, EVENT) -> Unit>()
        val transitions = linkedMapOf<Matcher<EVENT, EVENT>, (STATE, EVENT) -> TransitionTo<STATE, SIDE_EFFECT>>()

        data class TransitionTo<out STATE : Any, out SIDE_EFFECT : Any> internal constructor(
            val toState: STATE,
            val sideEffect: SIDE_EFFECT?
        )
    }
}
