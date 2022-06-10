@file:Suppress("UNCHECKED_CAST")

package com.onegravity.bloc

import com.onegravity.bloc.internal.BlocExtension
import com.onegravity.bloc.utils.*

/**
 * Submit an Initializer to a Bloc to be run.
 * The initializer will receive the state and a dispatch function.
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> Bloc<State, Action, SideEffect>.onCreate(
    initializer: Initializer<State, Action>
) {
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    (this as BlocExtension<State, Action, SideEffect, Proposal>).initialize(initializer)
}

/**
 * Submit an Initializer to a BlocOwner/Bloc to be run.
 * The initializer will receive the state and a dispatch function.
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.onCreate(
    initializer: Initializer<State, Action>
) {
    bloc.onCreate<State, Action, SideEffect, Proposal>(initializer)
}

/**
 * Submit a Thunk to a Bloc to be run.
 * The thunk will receive the dispatch and the getState function but no action (since it was
 * triggered "manually", not by sending an action to the Bloc).
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> Bloc<State, Action, SideEffect>.thunk(
    thunk: ThunkNoAction<State, Action>
) {
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    (this as BlocExtension<State, Action, SideEffect, Proposal>).thunk(thunk)
}

/**
 * Submit a Thunk to a BlocOwner/Bloc to be run.
 * The thunk will receive the dispatch and the getState function but no action (since it was
 * triggered "manually", not by sending an action to the Bloc).
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.thunk(
    thunk: ThunkNoAction<State, Action>
) {
    bloc.thunk<State, Action, SideEffect, Proposal>(thunk)
}

/**
 * Submit a Reducer without side effects to a Bloc to be run.
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
 * Submit a Reducer without side effects to a BlocOwner/Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.reduce(
    reducer: ReducerNoAction<State, Proposal>
) {
    bloc.reduce(reducer)
}

/**
 * Submit a Reducer with side effects to a Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> Bloc<State, Action, SideEffect>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) {
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    (this as BlocExtension<State, Action, SideEffect, Proposal>).reduce(reducer)
}

/**
 * Submit a Reducer with side effects to a BlocOwner/Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) {
    bloc.reduceAnd(reducer)
}

/**
 * Submit a SideEffect to a Bloc to be emitted.
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
    (this as BlocExtension<State, Action, SideEffect, Proposal>).reduce(reducerNoState)
}

/**
 * Submit a SideEffect to a BlocOwner/Bloc to be emitted.
 * The side effect will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) {
    bloc.sideEffect<State, Action, SideEffect, Proposal>(sideEffect)
}
