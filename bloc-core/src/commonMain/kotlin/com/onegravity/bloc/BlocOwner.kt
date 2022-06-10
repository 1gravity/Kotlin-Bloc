package com.onegravity.bloc

import com.onegravity.bloc.utils.BlocDSL
import com.onegravity.bloc.utils.Effect

/**
 * A class is a BlocOwner if it holds and exposes a Bloc.
 *
 * Using extension functions this interface allows the use of the short syntax for defining
 * thunk { }, sideEffect { } and reduce { } / reduceAnd { }, e.g.:
 *
 * ```
 *   fun onClicked(item: Item) = sideEffect {
 *       ItemList.OpenItem(list)
 *   }
 * ```
 *
 * If BlocOwner isn't implemented (e.g. because the Bloc shouldn't be visible outside the class),
 * the syntax is:
 * ```
 *   fun onClicked(item: Item) = bloc.sideEffect {
 *       ItemList.OpenItem(list)
 *   }
 * ```
 *
 * If a component is a BlocOwner it doesn't have to implement BlocObservableOwner since a BlocOwner
 * can also be observed (it has an observable Bloc) and every extension functions for
 * BlocObservableOwner is also implemented for BlocOwner.
 */
public interface BlocOwner<out State : Any, in Action : Any, SideEffect : Any, Proposal : Any> {

    public val bloc: Bloc<State, Action, SideEffect>

    /* Extension functions */

    @BlocDSL
    public fun Proposal.noSideEffect(): Effect<Proposal, SideEffect> =
        Effect(this, emptyList())

    @BlocDSL
    public infix fun Proposal.and(sideEffect: SideEffect): Effect<Proposal, SideEffect> =
        Effect(this, sideEffect)

    @BlocDSL
    public infix fun SideEffect.and(sideEffect: SideEffect): List<SideEffect> =
        listOf(this, sideEffect)

}
