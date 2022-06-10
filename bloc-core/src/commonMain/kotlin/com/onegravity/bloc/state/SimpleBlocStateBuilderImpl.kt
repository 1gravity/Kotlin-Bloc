package com.onegravity.bloc.state

internal class SimpleBlocStateBuilderImpl<State : Any> : SimpleBlocStateBuilder<State> {

    private var _initialState: State? = null

    override fun build() = DefaultBlocState<State, State>(
        initialState = checkNotNull(_initialState) { "initialState must be declared" },
        accept = { proposal, _ -> proposal }
    )

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

}