package com.onegravity.bloc.utils

import kotlin.jvm.JvmName

data class Effect<Proposal, SideEffect>(
    val proposal: Proposal?,
    val sideEffects: List<SideEffect>
) {

    @BlocDSL
    @JvmName("sideEffectAnd")
    infix fun and(sideEffect: SideEffect): Effect<Proposal, SideEffect> =
        Effect(proposal, sideEffects + sideEffect)

    @BlocDSL
    @JvmName("proposalAnd")
    infix fun and(proposal: Proposal): Effect<Proposal, SideEffect> =
        Effect(proposal, sideEffects)

}
