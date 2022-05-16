package com.onegravity.bloc.sample.counter

import com.onegravity.bloc.BlocBuilder
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Decrement
import com.onegravity.bloc.sample.counter.SimpleCounter.Action.Increment
import com.onegravity.bloc.state.asBlocState
import com.onegravity.bloc.utils.logger

/**
 * Demo to show how a Bloc can "act" as a BlocState (interceptorBloc).
 */
object SimpleCounter {
    sealed class Action {
        data class Increment(val value: Int = 1) : Action()
        data class Decrement(val value: Int = 1) : Action()
    }

    private fun BlocBuilder<Int, Int, Unit, Int>.counterReduce(addValue: Int) {
        reduce {
            logger.d("interceptor: $action -> ${action + addValue}")
            action + addValue
        }
    }

    private fun BlocContext.blocState(addValue: Int) = bloc<Int, Int>(this, 1) {
        counterReduce(addValue)
    }

    private fun BlocContext.interceptorBloc(addValue: Int) = bloc<Int, Int>(this, blocState(-1).asBlocState()) {
        counterReduce(addValue)
    }

    fun bloc(context: BlocContext) = bloc<Int, Action, Unit>(
        context,
        context.interceptorBloc(1).asBlocState()
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

    // short version
    fun blocSimple(context: BlocContext) = bloc<Int, Boolean>(context, 1) {
        reduce { state + if (action) 1 else -1 }
    }

    // counter that just increments
    fun blocInc(context: BlocContext) = bloc<Int, Unit>(context, 1) {
        reduce { state + 1 }
    }
}


