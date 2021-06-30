package com.genaku.reduce

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

class CoroutineKnotState<S : State>(initialState: S) : KnotState<S> {

//    private val lock = ReentrantReadWriteLock()
    private val _state = MutableStateFlow(initialState)

    override val state: StateFlow<S> = _state

    suspend fun changeState(newState: suspend (S) -> S) {
//        lock.write {
            _state.emit(newState(_state.value))
//        }
    }
}