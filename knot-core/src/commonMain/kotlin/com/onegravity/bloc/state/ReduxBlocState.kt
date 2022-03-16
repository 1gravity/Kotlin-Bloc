package com.onegravity.bloc.state

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import com.onegravity.bloc.utils.selectScoped
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import org.reduxkotlin.Store

class ReduxBlocState<State, Proposal: Any, Model: Any, ReduxModel: Any>(
    disposableScope: DisposableScope,
    initialState: State,
    private val store: Store<ReduxModel>,
    selector: Selector<ReduxModel, Model>,
    mapper: Mapper<Model, State>
) : BlocState<State, Proposal>,
    DisposableScope by disposableScope {

    private val state = MutableStateFlow(initialState)

    init {
        // using selectScoped will unsubscribe from the store automatically when the Bloc's
        // lifecycle ends (onDestroy() called)
        store.selectScoped(this, selector) { model ->
            state.tryEmit(mapper(model))
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
