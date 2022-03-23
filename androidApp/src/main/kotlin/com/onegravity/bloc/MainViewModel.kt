package com.onegravity.bloc

import com.onegravity.bloc.sample.MainMenu.ActionState
import com.onegravity.bloc.sample.MainMenu.bloc
import com.onegravity.bloc.utils.BlocObservableOwner
import com.onegravity.bloc.utils.toObservable

class MainViewModel(context: ActivityBlocContext) :
    BaseViewModel(context),
    BlocObservableOwner<ActionState, ActionState> {

    private val bloc = bloc(viewModelContext)

    override val observable = bloc.toObservable()

    fun onClick(action: ActionState) {
        bloc.emit(action)
    }

}