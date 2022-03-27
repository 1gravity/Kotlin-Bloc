package com.onegravity.bloc

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.sample.MainMenu.ActionState
import com.onegravity.bloc.sample.MainMenu.bloc
import com.onegravity.bloc.utils.BlocObservableOwner

class MainViewModel(activityBlocContext: ActivityBlocContext) :
    ViewModel(),
    BlocObservableOwner<ActionState, ActionState> {

    private val bloc = bloc(blocContext(activityBlocContext))

    override val observable = bloc.toObservable()

    fun onClick(action: ActionState) {
        bloc.send(action)
    }

}
