package com.onegravity.bloc.redux

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.MutableStateStream
import com.onegravity.bloc.utils.Selector
import kotlinx.coroutines.flow.FlowCollector
import org.reduxkotlin.Store

internal class ReduxBlocState<State : Any, Proposal : Any, Model : Any, ReduxModel : Any>(
    disposableScope: DisposableScope,
    initialState: State,
    private val store: Store<ReduxModel>,
    selector: Selector<ReduxModel, Model>,
    mapper: Mapper<Model, State>
) : BlocState<State, Proposal>(),
    DisposableScope by disposableScope {

    private val state = MutableStateStream(initialState)

    init {
        // using selectScoped will unsubscribe from the store automatically when the Bloc's
        // lifecycle ends (onDestroy() called)
        store.selectScoped(this, selector) { model ->
            state.send(mapper(model))
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
    override fun send(value: Proposal) {
        store.dispatch(value)
    }

}
