package com.onegravity.bloc.utils

import com.onegravity.bloc.Bloc

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
