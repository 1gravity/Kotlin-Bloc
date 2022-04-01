package com.onegravity.bloc.utils

/**
 * No we're not going to use a state machine for this...
 */
class BlocLifecycle {
    enum class State {
        NOT_STARTED,
        STARTED,
        DESTROYED
    }

    private var blocState = State.NOT_STARTED

    // not thread safe but Atomicfu doesn't seem to be maintained any more
    fun transition(expectedState: State, newState: State, block: () -> Unit) {
        blocState = when (blocState) {
            expectedState -> {
                block()
                newState
            }
            else -> blocState
        }
    }
}