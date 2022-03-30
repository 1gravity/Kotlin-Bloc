package com.onegravity.bloc.state

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.onegravity.bloc.utils.Sink
import com.onegravity.bloc.utils.StateStream

interface BlocState<out State, in Proposal> :
    StateStream<State>, Sink<Proposal>,
    InstanceKeeper.Instance
