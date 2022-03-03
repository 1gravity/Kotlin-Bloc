package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.state.CoroutineKnotState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * A configuration builder for suspend [Knot].
 **/
abstract class SuspendKnotBuilder<S : State, Intent, SideEffect> {

    protected var _dispatcher: CoroutineContext = Dispatchers.Default

    protected var _initialState: S? = null
    protected var _knotState: CoroutineKnotState<S>? = null
    protected var _reducer: SuspendReducer<S, Intent, SideEffect>? = null
    protected var _performer: SuspendPerformer<SideEffect, Intent>? = null

    abstract fun build(): Knot<S, Intent>

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

    /** SideEffect section for [StateIntent] related declarations. */
    fun reduce(reducer: SuspendReducer<S, Intent, SideEffect>) {
        _reducer = reducer
    }

    /** SideEffect section for [StateAction] related declarations. */
    fun actions(performer: SuspendPerformer<SideEffect, Intent>?) {
        _performer = performer
    }

    /** Set coroutine context dispatcher */
    fun dispatcher(dispatcher: CoroutineContext) {
        _dispatcher = dispatcher
    }

    /** Throws [IllegalStateException] with current [State] and given [StateIntent] in its message. */
    fun S.unexpected(intent: Intent): Nothing = error("Unexpected $intent in $this")

    /** Turns [State] into an [Effect] without [StateAction]. */
    val S.toEffect: Effect<S, SideEffect> get() = Effect(this)

    /** Combines [State] and [StateAction] into [Effect]. */
    operator fun S.plus(action: SideEffect) = Effect(this, listOf(action))

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
        block: WhenState.() -> Effect<S, SideEffect>
    ): Effect<S, SideEffect> =
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
        intent: Intent, block: WhenState.() -> Effect<S, SideEffect>
    ): Effect<S, SideEffect> =
        if (this is WhenState) block()
        else unexpected(intent)
}