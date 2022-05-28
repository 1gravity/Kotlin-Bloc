package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor

internal class BlocStateBuilderImpl<State: Any, Proposal: Any> : BlocStateBuilder<State, Proposal> {

    private var _initialState: State? = null
    private var _acceptor: Acceptor<Proposal, State>? = null

    internal fun build() = DefaultBlocState(
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