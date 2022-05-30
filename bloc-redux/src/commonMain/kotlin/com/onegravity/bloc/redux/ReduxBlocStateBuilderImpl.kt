package com.onegravity.bloc.redux

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import org.reduxkotlin.Store

internal open class ReduxBlocStateBuilderImpl<State : Any, Model : Any, ReduxModel : Any> :
    ReduxBlocStateBuilder<State, Model, ReduxModel> {

    private var _selector: Selector<ReduxModel, Model>? = null
    private var _mapper: Mapper<Model, State>? = null

    fun <Proposal : Any> build(
        disposableScope: DisposableScope,
        store: Store<ReduxModel>
    ): ReduxBlocState<State, Proposal, Model, ReduxModel> =
        ReduxBlocState(
            disposableScope = disposableScope,
            store = store,
            select = checkNotNull(_selector) { "select { } must be declared" },
            map = checkNotNull(_mapper) { "map { } must be declared" },
        )

    override fun select(selector: Selector<ReduxModel, Model>) {
        _selector = selector
    }

    override fun map(mapper: Mapper<Model, State>) {
        _mapper = mapper
    }

}
