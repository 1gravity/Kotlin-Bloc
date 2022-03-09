package com.onegravity.knot.state

import com.onegravity.knot.*
import com.onegravity.knot.select.select
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import org.reduxkotlin.Store

// TODO KnotState that uses a Redux Store to store state
class ReduxKnotState<State, Proposal: Any, Model: Any>(
    initialState: State,
    private val store: Store<Any>,
    selector: Selector<Any, Model>,
    private val mapper: Mapper<Model, State>
) : KnotState<State, Proposal> {

    // TODO make this Disposable
    init {
        val subscriber = store.select(selector) {
            state.tryEmit(mapper(it))
        }
    }

    private val state = MutableStateFlow(initialState)

    /**
     * The Stream<State>.
     */
    override val value: State
        get() = state.value

    override suspend fun collect(collector: FlowCollector<State>) {
        state.collect(collector)
    }

    /**
     * The Sink<Proposal>.
     */
    override fun emit(value: Proposal) {
        store.dispatch(value)
    }

}
