package com.genaku.reduce

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <S : State, C : StateIntent, A : StateAction> knot(
    block: CoroutineKnotBuilder<S, C, A>.() -> Unit
): KnotImpl<S, C, A> = CoroutineKnotBuilder<S, C, A>()
    .also(block)
    .build() as KnotImpl<S, C, A>

@Suppress("UNCHECKED_CAST")
fun <S : State, C : StateIntent> easyKnot(
    block: EasyCoroutineKnotBuilder<S, C>.() -> Unit
): KnotImpl<S, C, SideEffect<C>> = EasyCoroutineKnotBuilder<S, C>()
    .also(block)
    .build() as KnotImpl<S, C, SideEffect<C>>

@Suppress("UNCHECKED_CAST")
fun <S : State, C : StateIntent> suspendKnot(
    block: EasySuspendCoroutineKnotBuilder<S, C>.() -> Unit
): SuspendKnotImpl<S, C, SuspendSideEffect<C>> = EasySuspendCoroutineKnotBuilder<S, C>()
    .also(block)
    .build() as SuspendKnotImpl<S, C, SuspendSideEffect<C>>