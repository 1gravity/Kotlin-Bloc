package com.genaku.reduce

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CoroutineKnotState<S : State>(initialState: S) : KnotState<S> {

    private val _state = MutableStateFlow(initialState)

    override val state: StateFlow<S> = _state

    suspend fun changeState(newState: suspend (S) -> S) {
        _state.update { newState(_state.value) }
    }

    private inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        while (true) {
            val prevValue = value
            val nextValue = function(prevValue)
            if (compareAndSet(prevValue, nextValue)) {
                return
            }
        }
    }
}