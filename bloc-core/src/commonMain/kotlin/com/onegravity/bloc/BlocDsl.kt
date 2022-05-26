@file:Suppress("UNCHECKED_CAST")

package com.onegravity.bloc

import com.onegravity.bloc.utils.*
import kotlinx.coroutines.CoroutineScope

/**
 * Submit an Initializer to the Bloc to be run.
 * The initializer will receive the state and a dispatch function.
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> Bloc<State, Action, SideEffect>.onCreate(
    initializer: Initializer<State, Action>
) {
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    (this as BlocExtension<State, Action, SideEffect, Proposal>).runInitializer(initializer)
}

/**
 * Same for a BlocOwner.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.onCreate(
    initializer: Initializer<State, Action>
) {
    bloc.onCreate<State, Action, SideEffect, Proposal>(initializer)
}

/**
 * Submit a Thunk to the Bloc to be run.
 * The thunk will receive the dispatch and the getState function but no action (since it was
 * triggered "manually", not by sending an action to the Bloc).
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> Bloc<State, Action, SideEffect>.thunk(
    coroutineScope: CoroutineScope? = null,
    thunk: ThunkNoAction<State, Action>
) {
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    (this as BlocExtension<State, Action, SideEffect, Proposal>).runThunk(coroutineScope, thunk)
}

/**
 * Same for a BlocOwner.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.thunk(
    coroutineScope: CoroutineScope? = null,
    thunk: ThunkNoAction<State, Action>
) {
    bloc.thunk<State, Action, SideEffect, Proposal>(coroutineScope, thunk)
}

/**
 * Submit a Reducer without side effects to the Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> Bloc<State, Action, SideEffect>.reduce(
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
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.reduce(
    reducer: ReducerNoAction<State, Proposal>
) {
    bloc.reduce(reducer)
}

/**
 * Submit a Reducer with side effects to the Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> Bloc<State, Action, SideEffect>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) {
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    (this as BlocExtension<State, Action, SideEffect, Proposal>).runReducer(reducer)
}

/**
 * Same for a BlocOwner.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) {
    bloc.reduceAnd(reducer)
}

/**
 * Submit a SideEffect to the Bloc to be emitted.
 * The side effect will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> Bloc<State, Action, SideEffect>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) {
    val reducerNoState: ReducerNoAction<State, Effect<Proposal, SideEffect>> = {
        Effect(null, sideEffect.invoke(this))
    }
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    (this as BlocExtension<State, Action, SideEffect, Proposal>).runReducer(reducerNoState)
}

/**
 * Same for a BlocOwner.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) {
    bloc.sideEffect<State, Action, SideEffect, Proposal>(sideEffect)
}
