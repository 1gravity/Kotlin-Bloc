package com.genaku.reduce

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/** A configuration builder for a [Knot]. */
abstract class KnotBuilder<State : Any, Change : Any, Action : Any> {

    private var _dispatcher: CoroutineContext = Dispatchers.Default

    protected var _initialState: State? = null
    protected var _reducer: Reducer<State, Change, Action>? = null
    protected var _performer: Performer<Action, Change>? = null

    abstract fun build(): Knot<State, Change>

    var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

    /** A section for [Change] related declarations. */
    fun changes(reducer: Reducer<State, Change, Action>) {
        _reducer = reducer
    }

    /** A section for [Action] related declarations. */
    fun actions(performer: Performer<Action, Change>?) {
        _performer = performer
    }

    fun dispatcher(dispatcher: CoroutineContext) {
        _dispatcher = dispatcher
    }

    /** Throws [IllegalStateException] with current [State] and given [Change] in its message. */
    fun State.unexpected(change: Change): Nothing = error("Unexpected $change in $this")

    /** Turns [State] into an [Effect] without [Action]. */
    val State.only: Effect<State, Action> get() = Effect(this)

    /** Combines [State] and [Action] into [Effect]. */
    operator fun State.plus(action: Action) = Effect(this, listOf(action))

    /**
     * Executes given block if the knot is in the given state or
     * ignores the change in any other states.
     *
     * ```
     * reduce<Change> {
     *    whenState<State.Content> {
     *       ...
     *    }
     * }
     * ```
     * is a better readable alternative to
     * ```
     * reduce<Change> {
     *    when(this) {
     *       is State.Content -> ...
     *       else -> only
     *    }
     * }
     * ```
     */
    inline fun <reified WhenState : State> State.whenState(
        block: WhenState.() -> Effect<State, Action>
    ): Effect<State, Action> =
        if (this is WhenState) block()
        else Effect(this, emptyList())

    /**
     * Executes given block if the knot is in the given state or
     * throws [IllegalStateException] for the change in any other state.
     *
     * ```
     * reduce<Change> { change ->
     *    requireState<State.Content>(change) {
     *       ...
     *    }
     * }
     * ```
     * is a better readable alternative to
     * ```
     * reduce<Change> { change ->
     *    when(this) {
     *       is State.Content -> ...
     *       else -> unexpected(change)
     *    }
     * }
     * ```
     */
    inline fun <reified WhenState : State> State.requireState(
        change: Change, block: WhenState.() -> Effect<State, Action>
    ): Effect<State, Action> =
        if (this is WhenState) block()
        else unexpected(change)
}