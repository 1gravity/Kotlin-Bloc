package com.onegravity.knot

import com.onegravity.knot.builder.KnotBuilderImpl
import com.onegravity.knot.builder.EasyCoroutineKnotBuilder
import com.onegravity.knot.builder.EasySuspendCoroutineKnotBuilder

/** Creates a [Knot] instance. */
@Suppress("UNCHECKED_CAST")
fun <State, Action, Proposal> knot(
    block: KnotBuilderImpl<State, Action, Proposal>.() -> Unit
): KnotImpl<State, Intent, Proposal> = KnotBuilderImpl<State, Intent, Proposal>()
    .also(block)
    .build() as KnotImpl<S, Intent, Proposal>

@Suppress("UNCHECKED_CAST")
fun <State, Intent> easyKnot(
    block: EasyCoroutineKnotBuilder<State, Intent>.() -> Unit
): KnotImpl<State, Intent, SideEffect<Intent>> = EasyCoroutineKnotBuilder<State, Intent>()
    .also(block)
    .build() as KnotImpl<State, Intent, SideEffect<Intent>>

@Suppress("UNCHECKED_CAST")
fun <State, Intent> suspendKnot(
    block: EasySuspendCoroutineKnotBuilder<State, Intent>.() -> Unit
): SuspendKnotImpl<State, Intent, SuspendSideEffect<Intent>> = EasySuspendCoroutineKnotBuilder<State, Intent>()
    .also(block)
    .build() as SuspendKnotImpl<State, Intent, SuspendSideEffect<Intent>>