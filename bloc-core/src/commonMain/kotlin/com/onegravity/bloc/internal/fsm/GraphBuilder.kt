/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.internal.fsm

internal class GraphBuilder<STATE : Any, EVENT : Any, SIDE_EFFECT : Any>(
    graph: Graph<STATE, EVENT, SIDE_EFFECT>? = null
) {
    private var initialState = graph?.initialState
    private val stateDefinitions = LinkedHashMap(graph?.stateDefinitions ?: emptyMap())
    private val onTransitionListeners = ArrayList(graph?.onTransitionListeners
        ?: emptyList())

    internal fun initialState(initialState: STATE) {
        this.initialState = initialState
    }

    internal fun <S : STATE> state(
        stateMatcher: Matcher<STATE, S>,
        init: StateDefinitionBuilder<STATE, S, EVENT, SIDE_EFFECT>.() -> Unit
    ) {
        stateDefinitions[stateMatcher] =
            StateDefinitionBuilder<STATE, S, EVENT, SIDE_EFFECT>().apply(init).build()
    }

    internal inline fun <reified S : STATE> state(
        noinline init: StateDefinitionBuilder<STATE, S, EVENT, SIDE_EFFECT>.() -> Unit
    ) {
        state(Matcher.any(), init)
    }

    internal inline fun <reified S : STATE> state(
        state: S,
        noinline init: StateDefinitionBuilder<STATE, S, EVENT, SIDE_EFFECT>.() -> Unit
    ) {
        state(Matcher.eq(state), init)
    }

    internal fun onTransition(listener: (Transition<STATE, EVENT, SIDE_EFFECT>) -> Unit) {
        onTransitionListeners.add(listener)
    }

    internal fun build(): Graph<STATE, EVENT, SIDE_EFFECT> {
        return Graph(
            requireNotNull(initialState),
            stateDefinitions.toMap(),
            onTransitionListeners.toList()
        )
    }
}
