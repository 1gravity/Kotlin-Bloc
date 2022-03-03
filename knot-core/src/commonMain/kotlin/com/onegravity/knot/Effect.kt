package com.onegravity.knot

/** Convenience wrapper around [Model] and optional [SideEffect]s. */
data class Effect<Model, SideEffect>(
    val model: Model,
    val sideEffects: List<SideEffect> = emptyList()
) {
    operator fun plus(sideEffect: SideEffect) = Effect(model, sideEffects + sideEffect)
}