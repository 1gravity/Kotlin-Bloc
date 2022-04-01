package com.onegravity.bloc.counter

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.counter.ReduxCounter.Action.Decrement
import com.onegravity.bloc.sample.counter.ReduxCounter.Action.Increment
import com.onegravity.bloc.sample.counter.ReduxCounter.bloc
import com.onegravity.bloc.toLiveData

class CounterReduxViewModel(context: ActivityBlocContext) : ViewModel() {

    private val bloc = bloc(blocContext(context))

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