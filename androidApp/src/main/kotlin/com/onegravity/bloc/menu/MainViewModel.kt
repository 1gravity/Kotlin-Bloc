package com.onegravity.bloc.menu

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.MainMenu.ActionState
import com.onegravity.bloc.sample.MainMenu.bloc
import com.onegravity.bloc.utils.BlocObservableOwner
import com.onegravity.bloc.utils.toObservable

class MainViewModel :
    ViewModel(),
    BlocObservableOwner<ActionState, ActionState> {

    private val bloc = bloc(blocContext())

    override val observable = bloc.toObservable()

    fun onClick(action: ActionState) {
        bloc.send(action)
    }

}
