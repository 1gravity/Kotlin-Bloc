package com.onegravity.bloc.counter

import androidx.lifecycle.viewModelScope
import com.onegravity.bloc.BaseViewModel
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.sample.counter.ReduxCounter.knot
import com.onegravity.knot.sample.counter.ReduxCounter.Event.*
import com.onegravity.knot.toLiveData

class CounterReduxViewModel(context: ActivityKnotContext) : BaseViewModel(context) {

    private val knot = knot(viewModelContext)

    val state = knot.toLiveData(viewModelScope)

    fun increment() {
        knot.emit(Increment(1))
    }

    fun decrement() {
        knot.emit(Decrement(1))
    }

}