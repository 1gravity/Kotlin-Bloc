package com.onegravity.bloc.fsm

@Suppress("UNUSED")
sealed class Transition<out STATE : Any, out EVENT : Any, out SIDE_EFFECT : Any> {
    abstract val fromState: STATE
    abstract val event: EVENT

    data class Valid<out STATE : Any, out EVENT : Any, out SIDE_EFFECT : Any> internal constructor(
        override val fromState: STATE,
        override val event: EVENT,
        val toState: STATE,
        val sideEffect: SIDE_EFFECT?
    ) : Transition<STATE, EVENT, SIDE_EFFECT>()

    data class Invalid<out STATE : Any, out EVENT : Any, out SIDE_EFFECT : Any> internal constructor(
        override val fromState: STATE,
        override val event: EVENT
    ) : Transition<STATE, EVENT, SIDE_EFFECT>()
}