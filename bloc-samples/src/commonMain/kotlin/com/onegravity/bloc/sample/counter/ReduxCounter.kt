package com.onegravity.bloc.sample.counter

import com.onegravity.bloc.bloc
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.redux.toBlocState
import com.onegravity.bloc.sample.counter.CounterStore.ReduxAction
import com.onegravity.bloc.sample.counter.CounterStore.reduxStore

object ReduxCounter {
    sealed class Action(val value: Int)
    data class Increment(private val _value: Int = 1) : Action(_value)
    data class Decrement(private val _value: Int = 1) : Action(_value)

    fun bloc(context: BlocContext) = bloc<Int, Action, Nothing, ReduxAction>(
        context,
        reduxStore.toBlocState(
            context = context,
            select = { reduxModel ->  reduxModel.counter },
            map = { model -> model.count }
        )
    ) {
        reduce<Increment> { ReduxAction.UpdateCount(state + 1) }
        reduce<Decrement> { ReduxAction.UpdateCount(state - 1) }
    }
}
