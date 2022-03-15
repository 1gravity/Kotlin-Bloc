package com.onegravity.knot.sample.counter

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext

object SimpleCounter {
    sealed class Action {
        data class Increment(val value: Int = 1): Action()
        data class Decrement(val value: Int = 1): Action()
    }

    fun bloc(context: BlocContext) =
        bloc<Int, Action>(context, 1) {
            reduce { state, action ->
                when (action) {
                    is Action.Increment -> state + action.value
                    is Action.Decrement -> (state - action.value).coerceAtLeast(0)
                }
            }
        }

}
