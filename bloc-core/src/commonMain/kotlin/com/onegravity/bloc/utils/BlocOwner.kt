package com.onegravity.bloc.utils

import com.onegravity.bloc.Bloc

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
interface BlocOwner<out State, in Action, SideEffect, Proposal> {

    val bloc: Bloc<State, Action, SideEffect, Proposal>

    /* Extension functions */

    @BlocDSL
    fun Proposal.noSideEffect() = Effect<Proposal, SideEffect>(this, emptyList())

    @BlocDSL
    infix fun Proposal.and(sideEffect: SideEffect) = Effect(this, sideEffect)

    @BlocDSL
    infix fun SideEffect.and(sideEffect: SideEffect): List<SideEffect> = listOf(this, sideEffect)

}
