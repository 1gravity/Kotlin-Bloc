package com.onegravity.knot.state

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.onegravity.knot.Acceptor
import com.onegravity.knot.Mapper
import com.onegravity.knot.Selector
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.select.select
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import org.reduxkotlin.Store

class ReduxKnotState<State, Proposal: Any, Model: Any, ReduxModel: Any>(
    private val context: KnotContext,
    initialState: State,
    private val store: Store<ReduxModel>,
    selector: Selector<ReduxModel, Model>,
    private val acceptor: Acceptor<Model, State>,
    private val mapper: Mapper<Model, State>
) : KnotState<State, Proposal> {

    private val state = MutableStateFlow(initialState)

    init {
        context.lifecycle.doOnCreate {
            // subscribe to the Redux store
            val unsubscribe = store.select(selector) {
                state.tryEmit(mapper(it))
            }

            // unsubscribe from the Redux store
            context.lifecycle.doOnDestroy {
                unsubscribe.invoke()
            }
        }
    }

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
