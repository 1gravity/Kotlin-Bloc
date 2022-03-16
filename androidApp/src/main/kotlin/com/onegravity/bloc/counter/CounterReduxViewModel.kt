package com.onegravity.bloc.counter

import androidx.lifecycle.viewModelScope
import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.sample.counter.ReduxCounter.bloc
import com.onegravity.bloc.sample.counter.ReduxCounter.Action.*
import com.onegravity.bloc.toLiveData

class CounterReduxViewModel(context: ActivityBlocContext) : BaseViewModel(context) {

    private val bloc = bloc(viewModelContext)

    val state = bloc.toLiveData(viewModelScope)

    fun increment() {
        bloc.emit(Increment(1))
    }

    fun decrement() {
        bloc.emit(Decrement(1))
    }

}