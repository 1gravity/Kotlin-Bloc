package com.onegravity.bloc.utils

data class Effect<Proposal, SideEffect>(
    val proposal: Proposal?,
    val sideEffects: List<SideEffect>
) {

    constructor(proposal: Proposal?, sideEffect: SideEffect) : this(proposal, listOf(sideEffect))

    @BlocDSL
    infix fun and(sideEffect: SideEffect): Effect<Proposal, SideEffect> =
        Effect(proposal, sideEffects + sideEffect)

}
