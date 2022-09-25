/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.internal.fsm

internal class StateDefinitionBuilder<STATE : Any, S : STATE, EVENT : Any, SIDE_EFFECT : Any> {

    private val stateDefinition = Graph.State<STATE, EVENT, SIDE_EFFECT>()

    internal inline fun <reified E : EVENT> any(): Matcher<EVENT, E> = Matcher.any()

    internal inline fun <reified R : EVENT> eq(value: R): Matcher<EVENT, R> = Matcher.eq(value)

    internal fun <E : EVENT> on(
        eventMatcher: Matcher<EVENT, E>,
        createTransitionTo: S.(E) -> Graph.State.TransitionTo<STATE, SIDE_EFFECT>
    ) {
        stateDefinition.transitions[eventMatcher] = { state, event ->
            @Suppress("UNCHECKED_CAST")
            createTransitionTo((state as S), event as E)
        }
    }

    internal inline fun <reified E : EVENT> on(
        noinline createTransitionTo: S.(E) -> Graph.State.TransitionTo<STATE, SIDE_EFFECT>
    ) {
        return on(any(), createTransitionTo)
    }

    internal inline fun <reified E : EVENT> on(
        event: E,
        noinline createTransitionTo: S.(E) -> Graph.State.TransitionTo<STATE, SIDE_EFFECT>
    ) {
        return on(eq(event), createTransitionTo)
    }

    internal fun onEnter(listener: S.(EVENT) -> Unit) = with(stateDefinition) {
        onEnterListeners.add { state, cause ->
            @Suppress("UNCHECKED_CAST")
            listener(state as S, cause)
        }
    }

    internal fun onExit(listener: S.(EVENT) -> Unit) = with(stateDefinition) {
        onExitListeners.add { state, cause ->
            @Suppress("UNCHECKED_CAST")
            listener(state as S, cause)
        }
    }

    internal fun build() = stateDefinition

    @Suppress("UNUSED") // The unused warning is probably a compiler bug.
    internal fun S.transitionTo(state: STATE, sideEffect: SIDE_EFFECT? = null) =
        Graph.State.TransitionTo(state, sideEffect)

    @Suppress("UNUSED") // The unused warning is probably a compiler bug.
    internal fun S.dontTransition(sideEffect: SIDE_EFFECT? = null) = transitionTo(this, sideEffect)
}
