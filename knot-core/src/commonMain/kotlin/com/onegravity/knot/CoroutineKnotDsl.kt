package com.onegravity.knot

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <S : State, Intent, Proposal> knot(
    block: CoroutineKnotBuilder<S, Intent, Proposal>.() -> Unit
): KnotImpl<S, Intent, Proposal> = CoroutineKnotBuilder<S, Intent, Proposal>()
    .also(block)
    .build() as KnotImpl<S, Intent, Proposal>

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