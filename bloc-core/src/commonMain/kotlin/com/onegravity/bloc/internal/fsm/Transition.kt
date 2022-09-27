/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.internal.fsm

@Suppress("UNUSED")
internal sealed class Transition<out STATE : Any, out EVENT : Any, out SIDE_EFFECT : Any> {
    abstract val fromState: STATE
    abstract val event: EVENT

    internal data class Valid<out STATE : Any, out EVENT : Any, out SIDE_EFFECT : Any> internal constructor(
        override val fromState: STATE,
        override val event: EVENT,
        val toState: STATE,
        val sideEffect: SIDE_EFFECT?
    ) : Transition<STATE, EVENT, SIDE_EFFECT>()

    internal data class Invalid<out STATE : Any, out EVENT : Any, out SIDE_EFFECT : Any> internal constructor(
        override val fromState: STATE,
        override val event: EVENT
    ) : Transition<STATE, EVENT, SIDE_EFFECT>()
}
