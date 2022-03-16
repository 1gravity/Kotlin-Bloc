package com.onegravity.bloc.state

import com.onegravity.bloc.BlocFacade

interface BlocState<out State, in Proposal> : BlocFacade<State, Proposal>
