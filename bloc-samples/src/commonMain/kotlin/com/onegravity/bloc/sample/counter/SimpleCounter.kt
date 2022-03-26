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
        data class Increment(private val _value: Int = 1) : Action(_value)
        data class Decrement(private val _value: Int = 1) : Action(_value)
    }

    private fun BlocContext.interceptorBloc() = bloc<Int, Int>(this, 1) {
        reduce {
            logger.d("interceptor: $action -> ${action + 1}")
            action + 1
            action
        }
    }

    fun bloc(context: BlocContext) = bloc<Int, Action>(context, context.interceptorBloc()) {
        reduce<Action.Increment> { state + action.value }
        reduce<Action.Decrement> { (state - action.value).coerceAtLeast(0) }

        // does the same as the two reducers above
//            reduce {
//                when (action) {
//                    is Action.Increment -> state + action.value
//                    is Action.Decrement -> (state - action.value).coerceAtLeast(0)
//                }
//            }
    }

    // short version
    fun blocSimple(context: BlocContext) = bloc<Int, Boolean>(context, 1) {
        reduce { state + if (action) 1 else -1 }
    }

    // counter that just increments
    fun blocInc(context: BlocContext) = bloc<Int, Unit>(context, 1) {
        reduce { state + 1 }
    }
}


