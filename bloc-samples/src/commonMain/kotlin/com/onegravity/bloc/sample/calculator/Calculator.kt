package com.onegravity.bloc.sample.calculator

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.utils.Reducer

fun bloc(context: BlocContext) =
    bloc<State, Action>(context, State()) {
        reduce(reducer)
    }

private val reducer: Reducer<State, Action, State> = { state, action ->
    try {
        val newState = if (state.register1.isError() || state.register2.isError()) State() else state
        when (action) {
            is Action.Clear -> State()
            is Action.Add -> newState.apply(Operator.Add)
            is Action.Subtract -> newState.apply(Operator.Subtract)
            is Action.Multiply -> newState.apply(Operator.Multiply)
            is Action.Divide -> newState.apply(Operator.Divide)
            is Action.PlusMinus -> newState.plusMinus()
            is Action.Percentage -> newState.percentage()
            is Action.Digit -> newState.digit(action.digit)
            is Action.Period -> newState.period()
            is Action.Equals -> newState.equals()
        }
    } catch(ex: Exception) {
        State.error(ex.message)
    }
}

private fun State.apply(op: Operator): State {
    val state = if (register1.isNotEmpty() && register2.isNotEmpty()) equals() else this
    return state.copy(
        register1 = Register(),
        register2 = if (state.register1.isNotEmpty()) state.register1 else state.register2,
        operator = op
    )
}

private fun State.plusMinus() = copy(register1 = register1.plusMinus())

private fun State.period() = copy(register1 = register1.appendPeriod())

private fun State.digit(digit: Int) = copy(register1 = register1.appendDigit(digit))

private fun State.percentage() =
    if (register1.isEmpty()) this else copy(register1 = register1 / Register("100"))

private fun State.equals(): State {
    if (register2.isEmpty()) return this
    val reg1 = when(operator) {
        Operator.Add -> register2 + register1
        Operator.Subtract -> register2 - register1
        Operator.Multiply -> register2 * register1
        Operator.Divide -> register2 / register1
        else -> register1
    }
    return copy(register1 = reg1, register2 = Register(), operator = null)
}
