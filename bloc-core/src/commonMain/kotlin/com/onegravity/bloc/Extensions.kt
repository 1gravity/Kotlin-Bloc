package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector

/**
 * Subscribes to the state and side effects streams of a Bloc.
 *
 * The subscription is tied to the lifecycle of the caller. The subscription starts with onStart()
 * and is cancelled with onStop().

 */
@BlocDSL
fun <State, Action : Any, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) = bloc.subscribe(lifecycle, state, sideEffect)

@BlocDSL
fun <State, Action : Any, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

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
@BlocDSL
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
 * The assumption is that all Blocs use the same BlocState with the same type parameters (enforced
 * at compile time) but also that they share the same instance of a BlocState (not enforced at all).
 * Under that assumption we only need to observe the state of the first Bloc to observe all state
 * changes.
 * The edge-case that one passes in multiple Blocs using different BlocStates could be covered by
 * using a UUID for BlocState instances and then verify that all BlocStates have the same UUID but
 * that would be over-engineering imo.
 */
@BlocDSL
fun <State, Action : Any, SideEffect, Proposal> List<Bloc<State, Action, SideEffect, Proposal>>.toObservable() =
    object : BlocObservable<State, SideEffect> {
        override fun subscribe(
            lifecycle: Lifecycle,
            state: (suspend (state: State) -> Unit)?,
            sideEffect: (suspend (sideEffect: SideEffect) -> Unit)?
        ) {
            this@toObservable.forEachIndexed { index, bloc ->
                // ignore all but the first Bloc's state changes
                val stateListener = if (index == 0) state else { _ -> }
                bloc.subscribe(lifecycle, stateListener, sideEffect)
            }
        }
    }

/**
 * Same as above but combine just two Blocs to BlocObservable.
 */
@BlocDSL
fun <State, Action : Any, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.toObservable(
    bloc: Bloc<State, Action, SideEffect, Proposal>
) = listOf(this, bloc).toObservable()

/**
 * Call from a component to observe state and side effect updates in a BlocObservableOwner
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
@BlocDSL
fun <State, SideEffect> BlocObservableOwner<State, SideEffect>.subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(lifecycle, state, sideEffect)
}

/**
 * Submit an Initializer to the Bloc to be run.
 * The initializer will receive the state and a dispatch function.
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
fun <State, Action, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.onCreate(
    initializer: Initializer<State, Action>
) {
    (this as BlocImpl).runInitializer(initializer)
}

/**
 * Same for a BlocOwner.
 */
@BlocDSL
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.onCreate(
    initializer: Initializer<State, Action>
) = bloc.onCreate(initializer)

/**
 * Submit a Thunk to the Bloc to be run.
 * The thunk will receive the dispatch and the getState function but no action (since it was
 * triggered "manually", not by sending an action to the Bloc).
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
fun <State, Action, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.thunk(
    coroutineScope: CoroutineScope? = null,
    thunk: ThunkNoAction<State, Action>
) {
    (this as BlocImpl).runThunk(coroutineScope, thunk)
}

/**
 * Same for a BlocOwner.
 */
@BlocDSL
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.thunk(
    coroutineScope: CoroutineScope? = null,
    thunk: ThunkNoAction<State, Action>
) = bloc.thunk(coroutineScope, thunk)

/**
 * Submit a Reducer without side effects to the Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
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
@BlocDSL
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.reduce(
    reducer: ReducerNoAction<State, Proposal>
) = bloc.reduce(reducer)

/**
 * Submit a Reducer with side effects to the Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
fun <State, Action, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) {
    (this as BlocImpl).runReducer(reducer)
}

/**
 * Same for a BlocOwner.
 */
@BlocDSL
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) = bloc.reduceAnd(reducer)

/**
 * Submit a SideEffect to the Bloc to be emitted.
 * The side effect will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
fun <State, Action, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) {
    val reducerNoState: ReducerNoAction<State, Effect<Proposal, SideEffect>> = {
        Effect(null, sideEffect.invoke(this))
    }
    (this as BlocImpl).runReducer(reducerNoState)
}

/**
 * Same for a BlocOwner.
 */
@BlocDSL
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) = bloc.sideEffect(sideEffect)

/**
 * Adapts a Bloc to BlocState provided the Action type is the same as the Proposal type (what goes
 * in, must come out).
 */
fun <State, SideEffect, Proposal> Bloc<State, Proposal, SideEffect, Proposal>.asBlocState(): BlocState<State, Proposal> =
    object : BlocState<State, Proposal> {
        override fun send(value: Proposal) {
            this@asBlocState.send(value)
        }

        override val value: State
            get() = this@asBlocState.value

        override suspend fun collect(collector: FlowCollector<State>) {
            this@asBlocState.collect(collector)
        }
    }
