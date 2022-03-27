package com.onegravity.bloc.state

internal class SimpleBlocStateBuilderImpl<State> : SimpleBlocStateBuilder<State> {

    private var _initialState: State? = null

    override fun build() = BlocStateImpl<State, State>(
        initialState = checkNotNull(_initialState) { "initialState must be declared" },
        acceptor = { proposal, _ -> proposal }
    )

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

}