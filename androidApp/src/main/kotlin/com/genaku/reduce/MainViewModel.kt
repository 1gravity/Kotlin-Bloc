package com.genaku.reduce

import com.onegravity.bloc.Stream
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.sample.MainMenu.Action.*
import com.onegravity.knot.sample.MainMenu.State
import com.onegravity.knot.sample.MainMenu.bloc

class MainViewModel(context: ActivityKnotContext) : BaseViewModel(context) {

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