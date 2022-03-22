package com.onegravity.bloc.counter

import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Decrement
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Increment
import com.onegravity.bloc.sample.counter.SimpleCounter.bloc
import com.onegravity.bloc.toLiveData
import com.onegravity.bloc.utils.Logger
import com.onegravity.bloc.utils.subscribe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CounterSimpleViewModel(context: ActivityBlocContext) : BaseViewModel(context), KoinComponent {

    private val bloc = bloc(viewModelContext)

    private val logger by inject<Logger>()

    init {
        bloc.subscribe(
            state = {
                logger.i("state: $it")
            },
            sideEffect = {
                logger.i("side effect: $it")
            }
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