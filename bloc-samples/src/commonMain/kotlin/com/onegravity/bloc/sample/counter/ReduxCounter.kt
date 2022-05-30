package com.onegravity.bloc.sample.counter

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.redux.toBlocState
import com.onegravity.bloc.sample.counter.CounterStore.ReduxAction
import com.onegravity.bloc.sample.counter.CounterStore.reduxStore

object ReduxCounter {
    sealed class Action(val value: Int) {
        data class Increment(private val _value: Int = 1) : Action(_value)
        data class Decrement(private val _value: Int = 1) : Action(_value)
    }

    fun bloc(context: BlocContext) = bloc<Int, Action, Nothing, ReduxAction>(
        context,
        reduxStore.toBlocState(
            context = context,
            select = { reduxModel ->  reduxModel.counter },
            map = { model -> model.count }
        )
    ) {
        reduce<Action.Increment> { ReduxAction.UpdateCount(state + 1) }
        reduce<Action.Decrement> { ReduxAction.UpdateCount(state - 1) }
    }
}

// TODO think about the initial value, who/what provides it? should be have a mechanism to populate
//      the BlocState upon start? especially ReduxBlocState has TWO initial values (one for the
//      ReduxStore and one in the BlocState)
//      how about using the redux mechanism using an ActionTypes.INIT to initialize state?

// TODO think about navigation as well, will side effects be sufficient?
