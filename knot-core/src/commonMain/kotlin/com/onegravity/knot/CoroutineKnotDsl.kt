package com.onegravity.knot

import com.onegravity.knot.builder.CoroutineKnotBuilder
import com.onegravity.knot.builder.EasyCoroutineKnotBuilder
import com.onegravity.knot.builder.EasySuspendCoroutineKnotBuilder

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <State, Action, Proposal> knot(
    block: CoroutineKnotBuilder<State, Action, Proposal>.() -> Unit
): SimpleKnotImpl<State, Intent, Proposal> = CoroutineKnotBuilder<State, Intent, Proposal>()
    .also(block)
    .build() as SimpleKnotImpl<S, Intent, Proposal>

@Suppress("UNCHECKED_CAST")
fun <State, Intent> easyKnot(
    block: EasyCoroutineKnotBuilder<State, Intent>.() -> Unit
): SimpleKnotImpl<State, Intent, SideEffect<Intent>> = EasyCoroutineKnotBuilder<State, Intent>()
    .also(block)
    .build() as SimpleKnotImpl<State, Intent, SideEffect<Intent>>

@Suppress("UNCHECKED_CAST")
fun <State, Intent> suspendKnot(
    block: EasySuspendCoroutineKnotBuilder<State, Intent>.() -> Unit
): SuspendKnotImpl<State, Intent, SuspendSideEffect<Intent>> = EasySuspendCoroutineKnotBuilder<State, Intent>()
    .also(block)
    .build() as SuspendKnotImpl<State, Intent, SuspendSideEffect<Intent>>