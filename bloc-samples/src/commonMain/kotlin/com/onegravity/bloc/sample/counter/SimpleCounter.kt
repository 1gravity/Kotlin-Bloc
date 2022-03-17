package com.onegravity.bloc.sample.counter

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.utils.logger

/**
 * Demo to show how a Bloc can "act" as a BlocState (interceptorBloc) and how the thunk
 * routing / dispatching works.
 */
object SimpleCounter {
    sealed class Action {
        data class Increment(val value: Int = 1): Action()
        data class Decrement(val value: Int = 1): Action()
    }

    fun bloc(context: BlocContext) : Bloc<Int, Action, Int> {
        val interceptorBloc = bloc<Int, Int>(context, 1) {
            reduce { _, action ->
                logger.d("interceptor: $action -> ${action + 1}")
                action + 1
            }
        }

        return bloc<Int, Action, Int>(context, interceptorBloc) {
            // thunk 1
            thunkMatching<Action.Increment> { _, action, dispatch ->
                logger.d("thunk 1 started: $action")
                dispatch(action)                         // dispatches to thunk 3
                dispatch(Action.Decrement(1))      // dispatches to thunk 2
            }
            // thunk 2
            thunkMatching<Action.Decrement> { _, action, dispatch ->
                logger.d("thunk 2 started: $action")
                dispatch(Action.Decrement(3))      // dispatches to thunk 4
            }
            // thunk 3
            thunkMatching<Action.Increment> { _, action, dispatch ->
                logger.d("thunk 3 started: $action")
                dispatch(action)                         // dispatches to thunk 4
            }
            // thunk 4
            thunk { _, action, dispatch ->
                logger.d("thunk 4 started: $action")
                dispatch(action)                        // dispatches to thunk reduce
            }
            reduce { state, action ->
                logger.d("reduce started: $action")
                val result = when (action) {
                    is Action.Increment -> state + action.value
                    is Action.Decrement -> (state - action.value).coerceAtLeast(0)
                }
                result
            }
        }
    }

// shortest possible implementation
//    fun bloc(context: BlocContext) =
//        bloc<Int, Boolean>(context, 1) {
//            reduce { state, action -> state + if (action) 1 else -1 }
//        }

}
