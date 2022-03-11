package com.onegravity.knot.state

import com.onegravity.knot.Mapper
import com.onegravity.knot.Selector
import com.onegravity.knot.context.KnotContext
import org.reduxkotlin.Store

open class ReduxKnotStateBuilderImpl<State, Model: Any, ReduxModel: Any> :
    ReduxKnotStateBuilder<State, Model, ReduxModel> {

    private var _initialState: State? = null
    private var _selector: Selector<ReduxModel, Model>? = null
    private var _mapper: Mapper<Model, State>? = null

    fun <Proposal: Any> build(context: KnotContext, store: Store<ReduxModel>) : KnotState<State, Proposal> =
        ReduxKnotState(
            context = context,
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