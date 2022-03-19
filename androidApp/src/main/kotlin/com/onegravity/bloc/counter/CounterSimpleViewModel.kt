package com.onegravity.bloc.counter

import android.util.Log
import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Decrement
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Increment
import com.onegravity.bloc.sample.counter.SimpleCounter.bloc
import com.onegravity.bloc.toLiveData
import com.onegravity.bloc.utils.subscribe

class CounterSimpleViewModel(context: ActivityBlocContext) : BaseViewModel(context) {

    private val bloc = bloc(viewModelContext)

    init {
        bloc.subscribe(
            state = { Log.i("bloc", "state: $it") },
            sideEffect = { Log.i("bloc", "side effect: $it") }
        )
    }

    val state = toLiveData(bloc)
    //  alternatively:
    //  val state = bloc.toLiveData(viewModelScope)

    fun increment() {
        bloc.emit(Increment(1))
    }


    fun decrement() {
        bloc.emit(Decrement(1))
    }

}