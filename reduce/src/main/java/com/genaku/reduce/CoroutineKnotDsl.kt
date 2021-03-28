package com.genaku.reduce

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <S : State, C : Intent, A : Action> knot(
    block: CoroutineKnotBuilder<S, C, A>.() -> Unit
): KnotImpl<S, C, A> = CoroutineKnotBuilder<S, C, A>()
    .also(block)
    .build() as KnotImpl<S, C, A>
