/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.fsm

import com.onegravity.bloc.Matcher

class GraphBuilder<STATE : Any, EVENT : Any, SIDE_EFFECT : Any>(
    graph: Graph<STATE, EVENT, SIDE_EFFECT>? = null
) {
    private var initialState = graph?.initialState
    private val stateDefinitions = LinkedHashMap(graph?.stateDefinitions ?: emptyMap())
    private val onTransitionListeners = ArrayList(graph?.onTransitionListeners ?: emptyList())

    fun initialState(initialState: STATE) {
        this.initialState = initialState
    }

    fun <S : STATE> state(
        stateMatcher: Matcher<STATE, S>,
        init: StateDefinitionBuilder<STATE, S, EVENT, SIDE_EFFECT>.() -> Unit
    ) {
        stateDefinitions[stateMatcher] = StateDefinitionBuilder<STATE, S, EVENT, SIDE_EFFECT>().apply(init).build()
    }

    inline fun <reified S : STATE> state(noinline init: StateDefinitionBuilder<STATE, S, EVENT, SIDE_EFFECT>.() -> Unit) {
        state(Matcher.any(), init)
    }

    inline fun <reified S : STATE> state(state: S, noinline init: StateDefinitionBuilder<STATE, S, EVENT, SIDE_EFFECT>.() -> Unit) {
        state(Matcher.eq<STATE, S>(state), init)
    }

    fun onTransition(listener: (Transition<STATE, EVENT, SIDE_EFFECT>) -> Unit) {
        onTransitionListeners.add(listener)
    }

    fun build(): Graph<STATE, EVENT, SIDE_EFFECT> {
        return Graph(
            requireNotNull(initialState),
            stateDefinitions.toMap(),
            onTransitionListeners.toList()
        )
    }
}
