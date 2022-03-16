package com.onegravity.bloc.counter

import androidx.lifecycle.viewModelScope
import com.onegravity.bloc.BaseViewModel
import com.onegravity.knot.ActivityBlocContext
import com.onegravity.bloc.sample.counter.SimpleCounter.bloc
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.*
import com.onegravity.knot.toLiveData

class CounterSimpleViewModel(context: ActivityBlocContext) : BaseViewModel(context) {

    private val bloc = bloc(viewModelContext)

    val state = bloc.toLiveData(viewModelScope)

    fun increment() {
        bloc.emit(Increment(1))
    }

    fun decrement() {
        bloc.emit(Decrement(1))
    }

}