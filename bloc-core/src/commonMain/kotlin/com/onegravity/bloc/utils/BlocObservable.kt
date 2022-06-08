package com.onegravity.bloc.utils

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.subscribe

/**
 * A Bloc than can be observed by subscribing to state and side effect updates
 * (has a subscribe function).
 *
 * Note: the subscriptions is tied to the lifecycle of the caller.
 * It depends on the concrete implementations of the BlocObservable which transitions of the
 * lifecycle are relevant (typically onStart() and onStop()).
 */
public abstract class BlocObservable<out State : Any, out SideEffect : Any> {

    public abstract val value: State

    public abstract fun subscribe(
        lifecycle: Lifecycle,
        state: (suspend (state: State) -> Unit)? = null,
        sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
    )

}

/**
 * If a components implements the BlocObservableOwner interface it needs to provide
 * ```
 *   val observable: BlocObservable<State, SideEffect>
 * ```
 * This extension functions converts a Bloc into that BlocObservable:
 * ```
 *   override val observable = bloc.toObservable()
 * ```
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any> Bloc<State, Action, SideEffect>.toObservable():
        BlocObservable<State, SideEffect> = object : BlocObservable<State, SideEffect>() {
    override val value: State
        get() = this@toObservable.value

    override fun subscribe(
        lifecycle: Lifecycle,
        state: (suspend (state: State) -> Unit)?,
        sideEffect: (suspend (sideEffect: SideEffect) -> Unit)?
    ) {
        val bloc = this@toObservable
        bloc.subscribe(lifecycle, state, sideEffect)
    }
}

/**
 * The assumption is that all Blocs use the same BlocState with the same type parameters (enforced
 * at compile time) but also that they share the same instance of a BlocState (not enforced at all).
 * Under that assumption we only need to observe the state of the first Bloc to observe all state
 * changes.
 * The edge-case that one passes in multiple Blocs using different BlocStates could be covered by
 * using a UUID for BlocState instances and then verify that all BlocStates have the same UUID but
 * that would be over-engineering imo.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any> List<Bloc<State, Action, SideEffect>>.toObservable():
        BlocObservable<State, SideEffect> = object : BlocObservable<State, SideEffect>() {

    override val value: State
        get() = this@toObservable.first().value

    override fun subscribe(
        lifecycle: Lifecycle,
        state: (suspend (state: State) -> Unit)?,
        sideEffect: (suspend (sideEffect: SideEffect) -> Unit)?
    ) {
        this@toObservable.forEachIndexed { index, bloc ->
            // ignore all but the first Bloc's state changes
            val stateListener = if (index == 0) state else null
            bloc.subscribe(lifecycle, stateListener, sideEffect)
        }
    }
}

/**
 * Same as above but combine just two Blocs to BlocObservable.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any> Bloc<State, Action, SideEffect>.toObservable(
    bloc: Bloc<State, Action, SideEffect>
): BlocObservable<State, SideEffect> = listOf(this, bloc).toObservable()
