package com.genaku.reduce

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/** A configuration builder for a [Knot]. */
abstract class KnotBuilder<S : State, I : StateIntent, A : StateAction> {

    protected var _dispatcher: CoroutineContext = Dispatchers.Default

    protected var _initialState: S? = null
    protected var _knotState: CoroutineKnotState<S>? = null
    protected var _reducer: Reducer<S, I, A>? = null
    protected var _performer: Performer<A, I>? = null

    abstract fun build(): Knot<S, I>

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

    /** A section for [StateIntent] related declarations. */
    fun reduce(reducer: Reducer<S, I, A>) {
        _reducer = reducer
    }

    /** A section for [StateAction] related declarations. */
    fun actions(performer: Performer<A, I>?) {
        _performer = performer
    }

    /** Set coroutine context dispatcher */
    fun dispatcher(dispatcher: CoroutineContext) {
        _dispatcher = dispatcher
    }

    /** Throws [IllegalStateException] with current [State] and given [StateIntent] in its message. */
    fun S.unexpected(intent: I): Nothing = error("Unexpected $intent in $this")

    /** Turns [State] into an [Effect] without [StateAction]. */
    val S.stateOnly: Effect<S, A> get() = Effect(this)

    /** Combines [State] and [StateAction] into [Effect]. */
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
     * reduce {
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
     * reduce { intent ->
     *    requireState<State.Content>(intent) {
     *       ...
     *    }
     * }
     * ```
     * is a better readable alternative to
     * ```
     * reduce { intent ->
     *    when(this) {
     *       is State.Content -> ...
     *       else -> unexpected(intent)
     *    }
     * }
     * ```
     */
    inline fun <reified WhenState : S> S.requireState(
        intent: I, block: WhenState.() -> Effect<S, A>
    ): Effect<S, A> =
        if (this is WhenState) block()
        else unexpected(intent)
}