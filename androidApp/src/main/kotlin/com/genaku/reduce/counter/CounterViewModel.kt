package com.genaku.reduce.counter

import com.genaku.reduce.BaseViewModel
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.Stream
import com.onegravity.knot.sample.counter.Counter

class CounterViewModel(context: ActivityKnotContext) : BaseViewModel(context) {

    val knot = Counter.knot(viewModelContext)

    val state: Stream<Int> = knot

    fun increment() {
        knot.emit(Counter.Event.Increment)
    }

    fun decrement() {
        knot.emit(Counter.Event.Decrement)
    }

}