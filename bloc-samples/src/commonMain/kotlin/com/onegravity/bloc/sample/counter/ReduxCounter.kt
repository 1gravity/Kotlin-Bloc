package com.onegravity.bloc.sample.counter

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.utils.toBlocState

object ReduxCounter {
    sealed class Action {
        data class Increment(val value: Int = 1): Action()
        data class Decrement(val value: Int = 1): Action()
    }

    fun bloc(context: BlocContext) = bloc<Int, Action, ReduxAction>(
        context,
        reduxStore.toBlocState(context = context, initialState = 1, selector = { it })
    ) {
        reduce { _, action ->
            when (action) {
                // we could use the same actions for the BLoC and the redux store but in a real
                // application the BLoC would implement some business logic
                is Action.Increment -> ReduxAction.Increment(action.value)
                is Action.Decrement -> ReduxAction.Decrement(action.value)
            }
        }
    }
}

// TODO think about the initial value, who/what provides it?
// TODO navigation, routing!
// TODO iOS
