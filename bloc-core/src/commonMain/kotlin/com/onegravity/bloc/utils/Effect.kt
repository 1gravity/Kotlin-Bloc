package com.onegravity.bloc.utils

public data class Effect<Proposal : Any, SideEffect : Any>(
    val proposal: Proposal?,
    val sideEffects: List<SideEffect>
) {

    public constructor(proposal: Proposal?, sideEffect: SideEffect) : this(proposal, listOf(sideEffect))

    @BlocDSL
    public infix fun and(sideEffect: SideEffect): Effect<Proposal, SideEffect> =
        Effect(proposal, sideEffects + sideEffect)

}
