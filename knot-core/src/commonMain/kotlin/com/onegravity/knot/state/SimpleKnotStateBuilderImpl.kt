package com.onegravity.knot.state

class SimpleKnotStateBuilderImpl<State> : SimpleKnotStateBuilder<State> {

    private var _initialState: State? = null

    override fun build(): KnotState<State, State> =
        KnotStateImpl<State, State, State>(
            initialState = checkNotNull(_initialState) { "initialState must be declared" },
            acceptor = { _, proposal -> proposal },
            mapper = { it }
        )

    override var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

}