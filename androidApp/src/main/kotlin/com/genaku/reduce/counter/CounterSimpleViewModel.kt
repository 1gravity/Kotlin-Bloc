package com.genaku.reduce.counter

import androidx.lifecycle.viewModelScope
import com.genaku.reduce.BaseViewModel
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.sample.counter.SimpleCounter2.knot
import com.onegravity.knot.sample.counter.SimpleCounter2.Action.*
import com.onegravity.knot.toLiveData

class CounterSimpleViewModel(context: ActivityKnotContext) : BaseViewModel(context) {

    private val knot = knot(viewModelContext)

    val state = knot.toLiveData(viewModelScope)

    fun increment() {
        knot.emit(Increment(1))
    }

    fun decrement() {
        knot.emit(Decrement(1))
    }

}