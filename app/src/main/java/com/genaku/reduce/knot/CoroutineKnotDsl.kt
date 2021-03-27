package com.genaku.reduce.knot

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <State : Any, Change : Any, Action : Any> coroutineKnot(
    block: CoroutineKnotBuilder<State, Change, Action>.() -> Unit
): CoroutineKnot<State, Change, Action> = CoroutineKnotBuilder<State, Change, Action>()
    .also(block)
    .build() as CoroutineKnot<State, Change, Action>
