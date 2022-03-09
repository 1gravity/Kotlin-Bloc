package com.onegravity.knot.state

import com.onegravity.knot.Acceptor

class KnotStateBuilderImpl<State, Proposal> : KnotStateBuilder<State, Proposal> {

    private var _initialState: State? = null
    private var _acceptor: Acceptor<Proposal, State>? = null

    override fun build() = KnotStateImpl(
        initialState = checkNotNull(_initialState) { "initialState must be declared" },
        acceptor = checkNotNull(_acceptor) { "accept { } must be declared" },
    )

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

    override fun accept(acceptor: Acceptor<Proposal, State>) {
        _acceptor = acceptor
    }

}