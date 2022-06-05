package com.onegravity.bloc.redux

import com.badoo.reaktive.disposable.scope.DisposableScope
import com.onegravity.bloc.state.BlocStateBase
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.Selector
import org.reduxkotlin.Store

internal class ReduxBlocState<State : Any, Proposal : Any, Model : Any, ReduxModel : Any>(
    disposableScope: DisposableScope,
    private val store: Store<ReduxModel>,
    select: Selector<ReduxModel, Model>,
    map: Mapper<Model, State>
) : BlocStateBase<State, Proposal>(map(select(store.getState()))),
    DisposableScope by disposableScope {

    init {
        // selectScoped will unsubscribe from the store automatically when the Bloc is destroyed
        store.selectScoped(disposableScope = this, select = select) { model ->
            state.send(map(model))
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun send(value: Proposal) {
        store.dispatch(value)
    }

}
