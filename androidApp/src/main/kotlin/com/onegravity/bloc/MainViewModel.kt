package com.onegravity.bloc

import com.onegravity.bloc.sample.MainMenu.Action.*
import com.onegravity.bloc.sample.MainMenu.State
import com.onegravity.bloc.sample.MainMenu.bloc

class MainViewModel(context: ActivityBlocContext) : BaseViewModel(context) {

    private val bloc = bloc(viewModelContext)

    val state: Stream<State>
        get() = bloc

    fun counter1() {
        bloc.emit(Counter1)
    }

    fun counter2() {
        bloc.emit(Counter2)
    }

    fun books() {
        bloc.emit(Books)
    }

    fun traffic() {
        bloc.emit(Traffic)
    }

}