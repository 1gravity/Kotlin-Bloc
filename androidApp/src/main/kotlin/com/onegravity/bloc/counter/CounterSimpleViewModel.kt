package com.onegravity.bloc.counter

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Decrement
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Increment
import com.onegravity.bloc.sample.counter.SimpleCounter.bloc
import com.onegravity.bloc.toLiveData
import org.koin.core.component.KoinComponent

class CounterSimpleViewModel() : ViewModel(), KoinComponent {

    private val bloc = bloc(blocContext())

    val state = toLiveData(bloc)

    fun increment() {
        bloc.send(Increment(1))
    }

    fun decrement() {
        bloc.send(Decrement(1))
    }

}