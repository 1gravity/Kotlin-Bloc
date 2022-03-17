package com.onegravity.bloc.sample.calculator

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext

object Calculator {

    enum class Operator(val display: String) {
        Add("+"),
        Subtract("-"),
        Divide("/"),
        Multiply("*")
    }

    data class State(
        val register1: Register = Register(),
        val register2: Register = Register(),
        val operator: Operator? = null
    ) {
        override fun toString() = StringBuilder().apply {
            if (! register2.isEmpty()) append(register2)
            if (operator != null) append(operator.display)
            if (! register1.isEmpty()) append(register1)
        }.toString()
    }

    sealed class Action {
        object Clear: Action()
        object Add: Action()
        object Subtract: Action()
        object Multiply: Action()
        object Divide: Action()
        object PlusMinus: Action()
        object Percentage: Action()
        data class Digit(val digit: Int): Action()
        object Period: Action()
        object Equals: Action()
    }

    fun bloc(context: BlocContext) = bloc<State, Action>(context, State()) {
        reduce { state, action ->
            val result = when (action) {
                is Action.Clear -> State()
                is Action.Add -> state
                is Action.Subtract -> state
                is Action.Multiply -> state
                is Action.Divide -> state
                is Action.PlusMinus -> state
                is Action.Percentage -> state
                is Action.Digit -> state.copy(register1 = state.register1.appendDigit(action.digit))
                is Action.Period -> state
                is Action.Equals -> state
            }
            result
        }
    }

}
