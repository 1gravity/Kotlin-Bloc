@file:Suppress("UNCHECKED_CAST")

package com.onegravity.bloc

import com.onegravity.bloc.internal.BlocExtension
import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Effect
import com.onegravity.bloc.utils.ReducerNoAction
import com.onegravity.bloc.utils.SideEffectNoAction
import com.onegravity.bloc.utils.ThunkNoAction
import kotlin.jvm.JvmName

/**
 * Submit a Thunk to a Bloc to be run.
 * The thunk will receive the dispatch and the getState function but no action (since it was
 * triggered "manually", not by sending an action to the Bloc).
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal: Any>
        Bloc<State, Action, SideEffect>.thunk(thunk: ThunkNoAction<State, Action, Proposal>) {
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    // the proposal is irrelevant for a thunk so we set it to Unit
    (this as BlocExtension<State, Action, SideEffect, Proposal>).thunk(thunk)
}

/**
 * Submit a Thunk to a Bloc to be run.
 * Simplified version with Proposal = State.
 */
@BlocDSL
@JvmName("BlocThunkSimplified")
public fun <State : Any, Action : Any, SideEffect : Any>
        Bloc<State, Action, SideEffect>.thunk(thunk: ThunkNoAction<State, Action, State>) {
    (this as BlocExtension<State, Action, SideEffect, State>).thunk(thunk)
}

/**
 * Submit a Thunk to a BlocOwner/Bloc to be run.
 * The thunk will receive the dispatch and the getState function but no action (since it was
 * triggered "manually", not by sending an action to the Bloc).
 * The dispatch function dispatches to the first matching thunk/reducer/side-effect in the Bloc.
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any>
        BlocOwner<State, Action, SideEffect, Proposal>.thunk(
    thunk: ThunkNoAction<State, Action, Proposal>
) {
    bloc.thunk(thunk)
}

/**
 * Submit a Thunk to a BlocOwner/Bloc to be run.
 * Simplified version with Proposal = State.
 */
@BlocDSL
@JvmName("BlocOwnerThunkSimplified")
public fun <State : Any, Action : Any, SideEffect : Any>
        BlocOwner<State, Action, SideEffect, State>.thunk(
    thunk: ThunkNoAction<State, Action, State>
) {
    bloc.thunk(thunk)
}

/**
 * Submit a Reducer without side effects to a Bloc to be run.
 * The reducer will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any>
        Bloc<State, Action, SideEffect>.reduce(
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
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any>
        BlocOwner<State, Action, SideEffect, Proposal>.reduce(
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
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any>
        Bloc<State, Action, SideEffect>.reduceAnd(
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
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any>
        BlocOwner<State, Action, SideEffect, Proposal>.reduceAnd(
    reducer: ReducerNoAction<State, Effect<Proposal, SideEffect>>
) {
    bloc.reduceAnd(reducer)
}

/**
 * Submit a SideEffect to a Bloc to be emitted.
 * The side effect will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 * Note: the proposal is irrelevant for sideEffect so we set it to Unit
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any> Bloc<State, Action, SideEffect>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) {
    val reducerNoState: ReducerNoAction<State, Effect<Unit, SideEffect>> = {
        Effect(null, sideEffect.invoke(this))
    }
    // we assume that every class implementing Bloc also implements BlocExtension
    // since we provide all concrete Bloc implementations, this is guaranteed
    (this as BlocExtension<State, Action, SideEffect, Unit>).reduce(reducerNoState)
}

/**
 * Submit a SideEffect to a BlocOwner/Bloc to be emitted.
 * The side effect will receive the state but no action (since it was triggered "manually", not by
 * sending an action to the Bloc).
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any>
        BlocOwner<State, Action, SideEffect, Proposal>.sideEffect(
    sideEffect: SideEffectNoAction<State, SideEffect>
) {
    bloc.sideEffect(sideEffect)
}
