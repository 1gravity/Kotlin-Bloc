package com.onegravity.knot.builder

import com.onegravity.knot.*
import com.onegravity.knot.state.KnotState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/** A configuration builder for a [Knot]. */
abstract class KnotBuilder<State, Event, Proposal, SideEffect> {

    protected var _initialState: State? = null
    protected var _knotState: KnotState<State, Proposal>? = null
    protected var _reducer: Reducer<State, Event, Proposal, SideEffect>? = null
    protected var _executor: Executor<SideEffect, Event>? = null
    protected var _suspendReducer: SuspendReducer<State, Event, Proposal, SideEffect>? = null
    protected var _suspendExecutor: SuspendExecutor<SideEffect, Event>? = null
    protected var _dispatcherReduce: CoroutineContext = Dispatchers.Default
    protected var _dispatcherSideEffect: CoroutineContext = Dispatchers.Default

    abstract fun build(): Knot<State, Event, Proposal, SideEffect>

    var initialState: State
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _initialState = value
        }

    var knotState: KnotState<State, Proposal>
        @Deprecated("Write-only.", level = DeprecationLevel.HIDDEN)
        get() = throw UnsupportedOperationException()
        set(value) {
            _knotState = value
        }

    /** A section for [Event] related declarations. */
    fun reduce(reducer: Reducer<State, Event, Proposal, SideEffect>) {
        _reducer = reducer
    }

    /** A section for [Event] related declarations. */
    fun suspendReduce(reducer: SuspendReducer<State, Event, Proposal, SideEffect>) {
        _suspendReducer = reducer
    }

    /** A section for [Event] related declarations. */
    fun execute(executor: Executor<SideEffect, Event>?) {
        _executor = executor
    }

    /** A section for [Event] related declarations. */
    fun suspendExecute(executor: SuspendExecutor<SideEffect, Event>?) {
        _suspendExecutor = executor
    }

    /** Set coroutine context dispatcher */
    fun dispatcher(dispatcher: CoroutineContext) {
        _dispatcherReduce = dispatcher
        _dispatcherSideEffect = dispatcher
    }

    /** Set coroutine context dispatcher for the reduce function */
    fun dispatcherReduce(dispatcher: CoroutineContext) {
        _dispatcherReduce = dispatcher
    }

    /** Set coroutine context dispatcher for the execute function */
    fun dispatcherSideEffect(dispatcher: CoroutineContext) {
        _dispatcherSideEffect = dispatcher
    }

    // TODO remove this in favor of a result monad
    /** Throws [IllegalStateException] with current [State] and given [Event] in its message. */
    fun State.unexpected(action: Event): Nothing = error("Unexpected $action in $this")

    /** Turns [State] into an [Effect] without [SideEffect]. */
    val State.toEffect: Effect<State, SideEffect> get() = Effect(this)

    /** Combines [State] and [SideEffect] into [Effect]. */
    operator fun State.plus(sideEffect: SideEffect) = Effect(this, listOf(sideEffect))

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
    inline fun <reified WhenState : State> State.whenState(
        block: WhenState.() -> Effect<State, SideEffect>
    ): Effect<State, SideEffect> =
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
    inline fun <reified WhenState : State> State.requireState(
        action: Event, block: WhenState.() -> Effect<State, SideEffect>
    ): Effect<State, SideEffect> =
        if (this is WhenState) block()
        else unexpected(action)
}