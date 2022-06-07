package com.onegravity.bloc.counter

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.counter.ReduxCounter.Action.Decrement
import com.onegravity.bloc.sample.counter.ReduxCounter.Action.Increment
import com.onegravity.bloc.sample.counter.ReduxCounter.bloc
import com.onegravity.bloc.toLiveData

class CounterReduxViewModel : ViewModel() {

    // once we migrate to Kotlin 1.6.20 we can use multiple receivers and this will be more concise
    private val bloc = bloc(blocContext())

    val state = toLiveData(bloc)
    //  this does the same but it's more verbose:
//    val state = bloc.toLiveData(viewModelScope)

    fun increment() {
        bloc.send(Increment(1))
    }

    fun decrement() {
        bloc.send(Decrement(1))
    }

}