package com.onegravity.bloc.sample.counter

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.utils.logger

/**
 * Demo to show how a Bloc can "act" as a BlocState (interceptorBloc) and how the thunk
 * routing / dispatching works.
 */
object SimpleCounter {
    sealed class Action(val value: Int) {
        data class Increment(private val _value: Int = 1): Action(_value)
        data class Decrement(private val _value: Int = 1): Action(_value)
    }

    private fun BlocContext.interceptorBloc() = bloc<Int, Int>(this, 1) {
        reduce {
            logger.d("interceptor: $action -> ${action + 1}")
            action + 1
            action
        }
    }

    fun bloc(context: BlocContext) = bloc<Int, Action, String>(context, context.interceptorBloc()) {
        // thunk 1
//        thunk<Action.Increment> {
//            logger.d("thunk 1 started: $action")
//            dispatch(action)                         // dispatches to thunk 3
//            dispatch(Action.Decrement(1))      // dispatches to thunk 2
//        }
//        // thunk 2
//        thunk<Action.Decrement> {
//            logger.d("thunk 2 started: $action")
//            dispatch(Action.Decrement(3))      // dispatches to thunk 4
//        }
//        // thunk 3
//        thunk<Action.Increment> {
//            logger.d("thunk 3 started: $action")
//            dispatch(action)                         // dispatches to thunk 4
//        }
//        // thunk 4
//        thunk {
//            logger.d("thunk 4 started: $action")
//            dispatch(action)                        // dispatches to thunk reduce
//        }

        // sideEffect: reducer without state
        // state: reducer without side effect
        // reduce: reducer with side effect

        sideEffect<Action.Increment> {
            "Increment: ${action.value}"
        }

        sideEffect {
            "Hello World"
        }

        reduce<Action.Decrement> {
            (state - action.value).coerceAtLeast(0)
        }

//        reduce {
//            (state + action.value).coerceAtLeast(0)
//        }

        reduceAnd<Action.Increment> {
            state + action.value and "Increment: ${action.value}" and "Hello World"
//            state and "test"
//            "Hello World" and state and "Test"
        }

        reduceAnd {
            state + action.value and "Increment: ${action.value}" and "Hello World"
            state.noSideEffect and "test"
            "Hello World" and state and "Test"
        }

        // does the same as the two reducers above
//            reduce {
//                when (action) {
//                    is Action.Increment -> state + action.value
//                    is Action.Decrement -> (state - action.value).coerceAtLeast(0)
//                }
//            }
    }
}

// shortest possible implementation
//    fun bloc(context: BlocContext) =
//        bloc<Int, Boolean>(context, 1) {
//            reduce { state + if (action) 1 else -1 }
//        }
//    fun blocIncrement(context: BlocContext) =
//        bloc<Int, Unit>(context, 1) {
//            reduce { state + 1 }
//        }

