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
        reduxStore.toBlocState(context, 1) { it }
    ) {
        reduce { _, action ->
            when (action) {
                is Action.Increment -> ReduxAction.Increment(action.value)
                is Action.Decrement -> ReduxAction.Decrement(action.value)
            }
        }
    }
}

// TODO implement the books example the classic way and using Redux, also try to load the books from a Rest API
// TODO think about the initial value, who/what provides it?
// TODO navigation, routing!
// TODO iOS
