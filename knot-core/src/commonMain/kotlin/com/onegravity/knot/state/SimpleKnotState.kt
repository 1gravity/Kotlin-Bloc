package com.onegravity.knot.state

import com.onegravity.knot.Effect
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

class SimpleKnotState<Model, SideEffect>(initialState: Model) :
    KnotState<Model, Effect<Model, SideEffect>>,
    DisposableKnotState() {

    private val state = MutableStateFlow(initialState)

    override val value: Model
        get() = state.value

    override fun emit(value: Effect<Model, SideEffect>) {
        state.tryEmit(value.model)
    }

    override suspend fun collect(collector: FlowCollector<Model>): Nothing {
        state.collect(collector)
    }

}

