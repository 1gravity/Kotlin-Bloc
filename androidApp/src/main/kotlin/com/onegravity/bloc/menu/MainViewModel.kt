package com.onegravity.bloc.menu

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.MainMenu.ActionState
import com.onegravity.bloc.sample.MainMenu.bloc
import com.onegravity.bloc.toObservable
import com.onegravity.bloc.utils.BlocObservableOwner

class MainViewModel(context: ActivityBlocContext) :
    ViewModel(),
    BlocObservableOwner<ActionState, ActionState> {

    private val bloc = bloc(blocContext(context))

    override val observable = bloc.toObservable()

    fun onClick(action: ActionState) {
        bloc.send(action)
    }

}
