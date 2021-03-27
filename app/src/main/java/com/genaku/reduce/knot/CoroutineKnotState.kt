package com.genaku.reduce.knot

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

class CoroutineKnotState<State : Any>(initialState: State) : KnotState<State> {

    private val lock = ReentrantReadWriteLock()
    private val _state = MutableStateFlow(initialState)

    override val state: StateFlow<State> = _state

    suspend fun changeState(newState: (State) -> State) {
        lock.write {
            _state.emit(newState(_state.value))
        }
    }
}