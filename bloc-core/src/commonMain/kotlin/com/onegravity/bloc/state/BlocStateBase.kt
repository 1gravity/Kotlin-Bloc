package com.onegravity.bloc.state

import com.onegravity.bloc.utils.MutableStateStream
import kotlinx.coroutines.flow.FlowCollector

/**
 * A convenience base class for BlocState implementations.
 *
 * It implements all the BlocState function except send(Proposal).
 */
public abstract class BlocStateBase<State : Any, Proposal : Any>(
    initialState: State
) : BlocState<State, Proposal>() {

    protected val state: MutableStateStream<State> = MutableStateStream(initialState)

    override val value: State
        get() = state.value

    override suspend fun collect(collector: FlowCollector<State>) {
        state.collect(collector)
    }

}
