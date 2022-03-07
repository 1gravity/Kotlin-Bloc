package com.onegravity.knot

/** Convenience wrapper around [Proposal] and optional [SideEffect]s. */
data class Effect<Proposal, SideEffect>(
    val proposal: Proposal,
    val sideEffects: List<SideEffect> = emptyList()
) {
    operator fun plus(sideEffect: SideEffect) = Effect(proposal, sideEffects + sideEffect)
}