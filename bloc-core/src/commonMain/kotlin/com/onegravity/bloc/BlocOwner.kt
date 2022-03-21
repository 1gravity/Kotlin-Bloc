package com.onegravity.bloc

interface BlocOwner<out State, in Action, out SideEffect, out Proposal> {

    val bloc: Bloc<State, Action, SideEffect, Proposal>

}
