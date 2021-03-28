package com.genaku.reduce

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/** A configuration builder for a [Knot]. */
abstract class KnotBuilder<S : State, C : Intent, A : Action> {

    private var _dispatcher: CoroutineContext = Dispatchers.Default

    protected var _initialState: S? = null
    protected var _knotState: CoroutineKnotState<S>? = null
    protected var _reducer: Reducer<S, C, A>? = null
    protected var _performer: Performer<A, C>? = null

    abstract fun build(): Knot<S, C>

    var initialState: S
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

    var knotState: CoroutineKnotState<S>
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _knotState = value
        }

    /** A section for [C] related declarations. */
    fun intents(reducer: Reducer<S, C, A>) {
        _reducer = reducer
    }

    /** A section for [A] related declarations. */
    fun actions(performer: Performer<A, C>?) {
        _performer = performer
    }

    fun dispatcher(dispatcher: CoroutineContext) {
        _dispatcher = dispatcher
    }

    /** Throws [IllegalStateException] with current [S] and given [C] in its message. */
    fun S.unexpected(intent: C): Nothing = error("Unexpected $intent in $this")

    /** Turns [S] into an [Effect] without [A]. */
    val S.stateOnly: Effect<S, A> get() = Effect(this)

    /** Combines [S] and [A] into [Effect]. */
    operator fun S.plus(action: A) = Effect(this, listOf(action))

    /**
     * Executes given block if the knot is in the given state or
     * ignores the intent in any other states.
     *
     * ```
     * reduce<Intent> {
     *    whenState<State.Content> {
     *       ...
     *    }
     * }
     * ```
     * is a better readable alternative to
     * ```
     * reduce<Intent> {
     *    when(this) {
     *       is State.Content -> ...
     *       else -> only
     *    }
     * }
     * ```
     */
    inline fun <reified WhenState : S> S.whenState(
        block: WhenState.() -> Effect<S, A>
    ): Effect<S, A> =
        if (this is WhenState) block()
        else Effect(this, emptyList())

    /**
     * Executes given block if the knot is in the given state or
     * throws [IllegalStateException] for the intent in any other state.
     *
     * ```
     * reduce<Intent> { intent ->
     *    requireState<State.Content>(intent) {
     *       ...
     *    }
     * }
     * ```
     * is a better readable alternative to
     * ```
     * reduce<Intent> { intent ->
     *    when(this) {
     *       is State.Content -> ...
     *       else -> unexpected(intent)
     *    }
     * }
     * ```
     */
    inline fun <reified WhenState : S> S.requireState(
        intent: C, block: WhenState.() -> Effect<S, A>
    ): Effect<S, A> =
        if (this is WhenState) block()
        else unexpected(intent)
}