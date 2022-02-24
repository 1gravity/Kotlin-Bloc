package com.onegravity.knot

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <S : State, Intent, A : StateAction> knot(
    block: CoroutineKnotBuilder<S, Intent, A>.() -> Unit
): KnotImpl<S, Intent, A> = CoroutineKnotBuilder<S, Intent, A>()
    .also(block)
    .build() as KnotImpl<S, Intent, A>

@Suppress("UNCHECKED_CAST")
fun <S : State, Intent> easyKnot(
    block: EasyCoroutineKnotBuilder<S, Intent>.() -> Unit
): KnotImpl<S, Intent, SideEffect<Intent>> = EasyCoroutineKnotBuilder<S, Intent>()
    .also(block)
    .build() as KnotImpl<S, Intent, SideEffect<Intent>>

@Suppress("UNCHECKED_CAST")
fun <S : State, Intent> suspendKnot(
    block: EasySuspendCoroutineKnotBuilder<S, Intent>.() -> Unit
): SuspendKnotImpl<S, Intent, SuspendSideEffect<Intent>> = EasySuspendCoroutineKnotBuilder<S, Intent>()
    .also(block)
    .build() as SuspendKnotImpl<S, Intent, SuspendSideEffect<Intent>>