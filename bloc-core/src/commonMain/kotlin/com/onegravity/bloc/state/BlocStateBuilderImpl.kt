package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Acceptor

internal class BlocStateBuilderImpl<State : Any, Proposal : Any> :
    BlocStateBuilder<State, Proposal> {

    private var _initialState: State? = null
    private var _accept: Acceptor<Proposal, State>? = null

    internal fun build() = DefaultBlocState(
        initialState = checkNotNull(_initialState) { "initialState must be declared" },
        accept = checkNotNull(_accept) { "accept { } must be declared" },
    )

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

    override fun accept(accept: Acceptor<Proposal, State>) {
        _accept = accept
    }

}
