package com.genaku.reduce.counter

import androidx.lifecycle.viewModelScope
import com.genaku.reduce.BaseViewModel
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.sample.counter.Counter
import com.onegravity.knot.toLiveData

class CounterViewModel(context: ActivityKnotContext) : BaseViewModel(context) {

    private val knot = Counter.knot(viewModelContext)

    val state = knot.toLiveData(viewModelScope)

    fun increment() {
        knot.emit(Counter.Event.Increment)
    }

    fun decrement() {
        knot.emit(Counter.Event.Decrement)
    }

}