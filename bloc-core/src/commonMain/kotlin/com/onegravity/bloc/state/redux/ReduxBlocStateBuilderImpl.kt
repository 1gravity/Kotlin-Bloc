package com.onegravity.bloc.state.redux

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import org.reduxkotlin.Store

open class ReduxBlocStateBuilderImpl<State, Model: Any, ReduxModel: Any> :
    ReduxBlocStateBuilder<State, Model, ReduxModel> {

    private var _initialState: State? = null
    private var _selector: Selector<ReduxModel, Model>? = null
    private var _mapper: Mapper<Model, State>? = null

    fun <Proposal: Any> build(
        disposableScope: DisposableScope,
        store: Store<ReduxModel>
    ) : ReduxBlocState<State, Proposal, Model, ReduxModel> =
        ReduxBlocState(
            disposableScope = disposableScope,
            initialState = checkNotNull(_initialState) { "initialState must be declared" },
            store = store,
            selector = checkNotNull(_selector) { "select { } must be declared" },
            mapper = checkNotNull(_mapper) { "map { } must be declared" },
        )

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

    override fun select(selector: Selector<ReduxModel, Model>) {
        _selector = selector
    }

    override fun map(mapper: Mapper<Model, State>) {
        _mapper = mapper
    }

}
