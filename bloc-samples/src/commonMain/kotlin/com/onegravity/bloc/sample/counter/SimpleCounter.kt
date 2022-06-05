package com.onegravity.bloc.sample.counter

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Decrement
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Increment
import com.onegravity.bloc.state.asBlocState
import com.onegravity.bloc.utils.logger

/**
 * Demo to show how a Bloc can "act" as a BlocState.
 * As a matter of fact, we have 3 "intercepting" Blocs in this example.
 */
object SimpleCounter {
    sealed class Action {
        data class Increment(val value: Int = 1) : Action()
        data class Decrement(val value: Int = 1) : Action()
    }

    private fun BlocContext.interceptorBloc3() = bloc<Int, Int>(
        this,
        1
    ) {
        thunk {
            logger.d("interceptor 3 writing an audit trail asynchronously to a db")
            logger.d("Counter changing from ${getState()} to $action")
            dispatch(action)
        }
        reduce { action }
    }

    private fun BlocContext.interceptorBloc2(addValue: Int) = bloc<Int, Int>(
        this,
        interceptorBloc3().asBlocState()
    ) {
        reduce {
            logger.d("interceptor 2: $action -> ${action + addValue}")
            action + addValue
        }
    }

    private fun BlocContext.interceptorBloc1(addValue: Int) = bloc<Int, Int>(
        this,
        interceptorBloc2(-1).asBlocState()
    ) {
        reduce {
            logger.d("interceptor 1: $action -> ${action + addValue}")
            action + addValue
        }
    }

    fun bloc(context: BlocContext) = bloc<Int, Action, Unit>(
        context,
        context.interceptorBloc1(1).asBlocState()
    ) {
        reduce<Increment> { state + action.value }
        reduce<Decrement> { (state - action.value).coerceAtLeast(0) }

        // does the same as the two reducers above
//        reduce {
//            when (action) {
//                is Increment -> state + (action as Increment).value
//                is Decrement -> (state - (action as Decrement).value).coerceAtLeast(0)
//            }
//        }
    }
}

// alternative ways to implement a simple Counter, use `CounterKt.blocXYZ` from iOS

// counter using a Boolean as Action
fun blocBoolean(context: BlocContext) = bloc<Int, Boolean>(context, 1) {
    reduce { state + if (action) 1 else -1 }
}

// counter using an Int as Action
fun blocInt(context: BlocContext) = bloc<Int, Int>(context, 1) {
    reduce { state + action }
}

// counter that just increments
fun blocInc(context: BlocContext) = bloc<Int, Unit>(context, 1) {
    reduce { state + 1 }
}
