package com.onegravity.bloc

interface BlocState<out State, in Proposal> : BlocFacade<State, Proposal>
