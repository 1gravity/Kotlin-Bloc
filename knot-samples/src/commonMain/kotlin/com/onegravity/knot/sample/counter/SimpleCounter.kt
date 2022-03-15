package com.onegravity.knot.sample.counter

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.logger

object SimpleCounter {
    sealed class Action {
        data class Increment(val value: Int = 1): Action()
        data class Decrement(val value: Int = 1): Action()
    }

    fun bloc(context: BlocContext) =
        bloc<Int, Action>(context, 1) {
            thunkMatching<Action.Increment> { getState, action, dispatch ->
                logger.w("1 state: ${getState.invoke()}")
                dispatch(action)
                logger.w("2 state: ${getState.invoke()}")
            }
//            thunk { getState, action, dispatch ->
//                logger.w("3 state: ${getState.invoke()}")
//                dispatch(action)
//                logger.w("4 state: ${getState.invoke()}")
//            }
            reduce { state, action ->
                when (action) {
                    is Action.Increment -> state + action.value
                    is Action.Decrement -> (state - action.value).coerceAtLeast(0)
                }
            }
        }

    // shortest possible implementation
//    fun bloc(context: BlocContext) =
//        bloc<Int, Boolean>(context, 1) {
//            reduce { state, action -> state + if (action) 1 else -1 }
//        }

}
