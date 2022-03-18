package com.onegravity.bloc.sample.calculator

import com.onegravity.bloc.BlocBuilder
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.blocState

// if BlocBuilder and BlocImpl are merged and called BlocContainer then we can define it analogous Orbit
class Calculator(context: BlocContext) : BlocBuilder<State, Action, Action>() {

    fun reduce() = reduce {
        action
    }

    fun thunk() = thunk {
        dispatch(action)
        dispatcher
    }

}

fun BlocContext.bloc() = bloc<State, Action>(this, State()) {
    fun State.resetErrors() = if (register1.isError() || register2.isError()) State() else this

    reduce<Action.Equals> { state.resetErrors().equals() }

    reduce<Action.Clear> { State() }

    reduce<Action.Add> { state.resetErrors().apply(Operator.Add) }

    reduce<Action.Subtract> { state.resetErrors().apply(Operator.Subtract) }

    reduce<Action.Multiply> { state.resetErrors().apply(Operator.Multiply) }

    reduce<Action.Divide> { state.resetErrors().apply(Operator.Divide) }

    reduce<Action.PlusMinus> { state.resetErrors().plusMinus() }

    reduce<Action.Percentage> { state.resetErrors().percentage() }

    /**
     * We can either define reducers per Action (see above) or define a reducer for multiple actions
     */
    reduce {
        try {
            val newState = state.resetErrors()
            when (action) {
                is Action.Digit -> newState.digit((action as Action.Digit).digit)
                is Action.Period -> newState.period()
                else -> state
            }
        } catch(ex: Exception) {
            State.error(ex.message)
        }
    }
}

private fun State.apply(op: Operator): State {
    return try {
        val state = if (register1.isNotEmpty() && register2.isNotEmpty()) equals() else this
        if (state.register1.isEmpty())
            state
        else
            state.copy(
                register1 = Register(),
                register2 = if (state.register1.isNotEmpty()) state.register1 else state.register2,
                operator = op
            )
    } catch(ex: Exception) {
        State.error(ex.message)
    }
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
