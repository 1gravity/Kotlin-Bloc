package com.onegravity.bloc.redux

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import org.reduxkotlin.Store

/**
 * Builder for a ReduxBlocState (for internal use only).
 */
internal class ReduxBlocStateBuilderImpl<State : Any, Model : Any, ReduxModel : Any> :
    ReduxBlocStateBuilder<State, Model, ReduxModel> {

    private var selector: Selector<ReduxModel, Model>? = null
    private var mapper: Mapper<Model, State>? = null

    fun <Proposal : Any> build(
        disposableScope: DisposableScope,
        store: Store<ReduxModel>
    ): ReduxBlocState<State, Proposal, Model, ReduxModel> =
        ReduxBlocState(
            disposableScope = disposableScope,
            store = store,
            select = checkNotNull(selector) { "select { } must be declared" },
            map = checkNotNull(mapper) { "map { } must be declared" },
        )

    override fun select(selector: Selector<ReduxModel, Model>) {
        this.selector = selector
    }

    override fun map(mapper: Mapper<Model, State>) {
        this.mapper = mapper
    }

}
