package com.onegravity.bloc.state

import com.onegravity.bloc.utils.Sink
import com.onegravity.bloc.utils.StateStream

interface BlocState<out State: Any, in Proposal: Any> : StateStream<State>, Sink<Proposal>