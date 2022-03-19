package com.onegravity.bloc.sample.stock

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.utils.logger

/**
 * Demo to show how a Bloc can "act" as a BlocState (interceptorBloc) and how the thunk
 * routing / dispatching works.
 */
object StockList {
    sealed class Action(val value: Int) {
        data class Increment(private val _value: Int = 1): Action(_value)
        data class Decrement(private val _value: Int = 1): Action(_value)
    }

    fun bloc(context: BlocContext) : Bloc<Int, Action, Nothing, Int> {
        val interceptorBloc = bloc<Int, Int>(context, 1) {
            reduce {
                logger.d("interceptor: $action -> ${action + 1}")
                action + 1
            }
        }

        return bloc<Int, Action, Int>(context, interceptorBloc) {
            // thunk 1
            thunk<Action.Increment> {
                logger.d("thunk 1 started: $action")
                dispatch(action)                         // dispatches to thunk 3
                dispatch(Action.Decrement(1))      // dispatches to thunk 2
            }
            // thunk 2
            thunk<Action.Decrement> {
                logger.d("thunk 2 started: $action")
                dispatch(Action.Decrement(3))      // dispatches to thunk 4
            }
            // thunk 3
            thunk<Action.Increment> {
                logger.d("thunk 3 started: $action")
                dispatch(action)                         // dispatches to thunk 4
            }
            // thunk 4
            thunk {
                logger.d("thunk 4 started: $action")
                dispatch(action)                        // dispatches to thunk reduce
            }
            reduce {
                logger.d("reduce started: $action")
                val result = when (action) {
                    is Action.Increment -> state + action.value
                    is Action.Decrement -> (state - action.value).coerceAtLeast(0)
                }
                result
            }
        }
    }

}
