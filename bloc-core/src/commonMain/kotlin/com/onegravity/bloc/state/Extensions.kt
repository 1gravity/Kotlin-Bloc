@file:Suppress("UNCHECKED_CAST")

package com.onegravity.bloc.state

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.flow.FlowCollector

/**
 * Adapts a Bloc to BlocState provided the Action type is the same as the Proposal type
 * (what goes in, must come out).
 */
fun <State : Any, SideEffect : Any, Proposal : Any> Bloc<State, Proposal, SideEffect>.asBlocState(): BlocState<State, Proposal> =
    object : BlocState<State, Proposal>() {
        override fun send(value: Proposal) {
            this@asBlocState.send(value)
        }

        override val value: State
            get() = this@asBlocState.value

        override suspend fun collect(collector: FlowCollector<State>) {
            this@asBlocState.collect(collector)
        }
    }
