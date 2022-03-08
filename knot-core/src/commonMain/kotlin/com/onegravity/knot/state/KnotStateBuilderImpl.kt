package com.onegravity.knot.state

import com.onegravity.knot.Acceptor
import com.onegravity.knot.Mapper

class KnotStateBuilderImpl<State, Proposal, Model> : KnotStateBuilder<State, Proposal, Model> {

    private var _initialState: State? = null
    private var _acceptor: Acceptor<State, Proposal, Model>? = null
    private var _mapper: Mapper<Model, State>? = null

    override fun build(): KnotState<State, Proposal> =
        KnotStateImpl(
            initialState = checkNotNull(_initialState) { "initialState must be declared" },
            acceptor = checkNotNull(_acceptor) { "accept { } must be declared" },
            mapper = checkNotNull(_mapper) { "map { } must be declared" },
        )

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

    override fun accept(acceptor: Acceptor<State, Proposal, Model>) {
        _acceptor = acceptor
    }

    override fun map(mapper: Mapper<Model, State>) {
        _mapper = mapper
    }

}