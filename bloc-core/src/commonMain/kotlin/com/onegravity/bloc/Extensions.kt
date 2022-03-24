package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*

/**
 * Subscribes to the state and side effects streams of a Bloc.
 *
 * The subscription is tied to the lifecycle of the caller. The subscription starts with onStart()
 * and is cancelled with onStop().

 */
fun <State, Action : Any, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    lifecycle.doOnStart {
        logger.d("onStart -> start subscription")
        state?.let {
            coroutineScope.launch {
                collect { state(it) }
            }
        }
        sideEffect?.let {
            coroutineScope.launch {
                sideEffects.collect { sideEffect(it) }
            }
        }
    }

    lifecycle.doOnStop {
        logger.d("onStop -> stop subscription")
        coroutineScope.cancel("Stop Subscription")
    }
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
fun <State, Action : Any, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.toObservable() =
    object : BlocObservable<State, SideEffect> {
        override fun subscribe(
            lifecycle: Lifecycle,
            state: (suspend (state: State) -> Unit)?,
            sideEffect: (suspend (sideEffect: SideEffect) -> Unit)?
        ) {
            this@toObservable.subscribe(lifecycle, state, sideEffect)
        }
    }

/**
 * Call in a component to observe state and side effect updates in a BlocObservableOwner
 * (BlocObservableOwner in Android is typically a ViewModel, the observing component a Fragment or
 * an Activity):
 * ```
 *   component.subscribe(lifecycle, state = ::render, sideEffect = ::sideEffect)
 * ```
 *
 * Note: there are extension functions for Fragments and Activities to get an Essenty lifecycle,
 * so a call typically looks like:
 * ```
 *   component.subscribe(this, state = ::render, sideEffect = ::sideEffect)
 * ```
 */
fun <State, SideEffect> BlocObservableOwner<State, SideEffect>.subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(lifecycle, state, sideEffect)
}

/**
 * Submit a Thunk to the Bloc to be run.
 * The thunk will receive the dispatch and the getState function but no action (since it was
 * triggered "manually", not by sending an action to the Bloc).
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
fun <State, Action, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.thunk(
    thunk: ThunkNoAction<State, Action>
) {
    (this as BlocImpl).coroutineScope.launch {
        runThunk(thunk)
    }
}

/**
 * Same for a BlocOwner.
 */
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.thunk(
    thunk: ThunkNoAction<State, Action>
) = bloc.thunk(thunk)

/**
 * Submit a Reducer without side effects to the Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
fun <State, Action, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.reduce(
    reducer: ReducerNoAction<State, Proposal>
) {
    val reducerNoSideEffect: ReducerNoAction<State, Effect<Proposal, SideEffect>> = {
        val proposal = reducer.invoke(this)
        Effect(proposal, emptyList())
    }
    reduceAnd(reducerNoSideEffect)
}

/**
 * Same for a BlocOwner.
 */
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.reduce(
    reducer: ReducerNoAction<State, Proposal>
) = bloc.reduce(reducer)

/**
 * Submit a Reducer to the Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
fun <State, Action, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) {
    (this as BlocImpl).coroutineScope.launch {
        runReducer(reducer)
    }
}

/**
 * Same for a BlocOwner.
 */
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) = bloc.reduceAnd(reducer)

/**
 * Submit a SideEffect to the Bloc to be emitted.
 * The side effect will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
fun <State, Action, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) {
    val reducerNoState: ReducerNoAction<State, Effect<Proposal, SideEffect>> = {
        Effect(null, sideEffect.invoke(this))
    }
    (this as BlocImpl).coroutineScope.launch {
        runReducer(reducerNoState)
    }
}

/**
 * Same for a BlocOwner.
 */
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) = bloc.sideEffect(sideEffect)
