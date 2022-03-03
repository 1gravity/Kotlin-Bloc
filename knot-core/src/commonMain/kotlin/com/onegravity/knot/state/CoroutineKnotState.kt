package com.onegravity.knot.state

import com.onegravity.knot.StreamReceiver
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Model and State are the same here.
 * TODO we need a builder that allows to define the two functions accept and the mapping from State to Model
 * TODO we also want a KnotState implementation that connects to a Redux Store:
 *   - accept, map, select functions
 *
 *
 */

/*
   we can also build this class using the tbd DSL for knotState
   knotState<Proposal, State, Model> {
       accept {
       }
       map {
       }
       select { <-- optional, only with Redux
       }
   }
*/

class SimpleCoroutineKnotState<State>(initialState: State) :
    KnotState<State, State>,
    DisposableKnotState() {

    private val state = MutableStateFlow(initialState)

    override fun send(value: State) {
        state.tryEmit((value))
    }

    suspend fun sendSuspend(value: State) {
        state.emit((value))
    }

    override val value: State
        get() = state.value

    override fun receive(receiver: StreamReceiver<State>) {
        state.collect {


        }
        TODO("Not yet implemented")
    }

//    internal suspend fun changeState(newState: suspend (State) -> State) {
//        _state.emit(newState(_state.value))
//    }
//    override val state: StateFlow<State> = _state
//
//
//    override val sink: Sink<State>
//        get() = TODO("Not yet implemented")
//
//    override val stream: Stream<State>
//        get() = TODO("Not yet implemented")
//
}

