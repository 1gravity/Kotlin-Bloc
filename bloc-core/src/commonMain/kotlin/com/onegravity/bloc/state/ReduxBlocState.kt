package com.onegravity.bloc.state

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.utils.MutableStream
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import com.onegravity.bloc.utils.selectScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import org.reduxkotlin.Store

class ReduxBlocState<State, Proposal: Any, Model: Any, ReduxModel: Any>(
    disposableScope: DisposableScope,
    initialState: State,
    private val store: Store<ReduxModel>,
    selector: Selector<ReduxModel, Model>,
    mapper: Mapper<Model, State>
) : BlocState<State, Proposal>,
    DisposableScope by disposableScope {

    private val state = MutableStream(initialState, 1)

    // we need this to execute Thunks, it's tied to the DisposableScope which is tied to the
    // lifecycle of the BlocContext
    // TODO use the builder to set a custom Dispatcher
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        // using selectScoped will unsubscribe from the store automatically when the Bloc's
        // lifecycle ends (onDestroy() called)
        store.selectScoped(this, selector) { model ->
            state.emit(mapper(model))
        }

        scope {
            coroutineScope.cancel("Stop BlocState")
        }
    }

    /**
     * The Stream<State>.
     */
    override val value: State
        get() = state.value

    override val replayCache: List<State>
        get() = state.replayCache

    override suspend fun collect(collector: FlowCollector<State>): Nothing {
        state.collect(collector)
    }

    /**
     * The Sink<Proposal>.
     */
    override fun emit(value: Proposal) {
        store.dispatch(value)
    }

}
