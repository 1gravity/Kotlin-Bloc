package com.onegravity.knot.state

import com.onegravity.knot.Acceptor
import com.onegravity.knot.Effect
import com.onegravity.knot.Mapper
import com.onegravity.knot.SideEffect
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

// TODO KnotState that used a Redux Store to store state
//abstract class ReduxStateImpl<State, Action, Model>(
//    initialState: State,
//    private val acceptor: Acceptor<State, Action, Model>,
//    private val mapper: Mapper<Model, State>
//) :
//    KnotState<State, Action>,
//    DisposableKnotState() {
//
//    private val state = MutableStateFlow(initialState)
//
//    override val value: State
//        get() = state.value
//
//    override suspend fun collect(collector: FlowCollector<State>): Nothing {
//        state.collect(collector)
//    }
//
//    override fun emit(value: Action) {
//        val model = acceptor(state.value, value)
//        val newState = mapper(model)
//        state.tryEmit(newState)
//    }
//
//}
//
