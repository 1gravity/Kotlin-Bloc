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
        reduxStore.toBlocState(context = context, initialState = 1)
    ) {
        reduce<Action.Increment> { ReduxAction.Increment(action.value) }
        reduce<Action.Decrement> { ReduxAction.Decrement(action.value) }
    }
}

// this would be the implementation using a simple ReduxStore where State == Action
//object ReduxCounter {
//    sealed class Action(val value: Int) {
//        data class Increment(private val _value: Int = 1): Action(_value)
//        data class Decrement(private val _value: Int = 1): Action(_value)
//    }
//    fun bloc(context: BlocContext) = bloc<Int, Action, Nothing, Int>(
//        context, context.reduxBlocState(1)
//    ) {
//        reduce<Action.Increment> { state + action.value }
//        reduce<Action.Decrement> { state - action.value }
//    }
//}

// TODO think about the initial value, who/what provides it? should be have a mechanism to populate
//      the BlocState upon start? especially ReduxBlocState has TWO initial values (one for the
//      ReduxStore and one in the BlocState)
//      how about using the redux mechanism using an ActionTypes.INIT to initialize state?

// TODO think about navigation as well, will side effects be sufficient?
