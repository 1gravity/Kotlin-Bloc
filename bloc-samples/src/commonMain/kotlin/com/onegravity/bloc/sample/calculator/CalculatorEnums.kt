package com.onegravity.bloc.sample.calculator

import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.bloc

/**
 * In this example we're using an Enum as Action. This allows us to bind the layouts directly to the
 * Enum without extra transformation.
 */
fun blocEnum(context: BlocContext) = bloc<CalculatorState, ActionEnum>(context, CalculatorState()) {
    fun CalculatorState.resetErrors() =
        if (register1.isError() || register2.isError()) CalculatorState() else this

    reduce(ActionEnum.Clear) { CalculatorState() }
    reduce(ActionEnum.Equals) { state.resetErrors().equals() }
    reduce(ActionEnum.Add) { state.resetErrors().apply(Operator.Add) }
    reduce(ActionEnum.Subtract) { state.resetErrors().apply(Operator.Subtract) }
    reduce(ActionEnum.Multiply) { state.resetErrors().apply(Operator.Multiply) }
    reduce(ActionEnum.Divide) { state.resetErrors().apply(Operator.Divide) }
    reduce(ActionEnum.PlusMinus) { state.resetErrors().plusMinus() }
    reduce(ActionEnum.Percentage) { state.resetErrors().percentage() }
    reduce(ActionEnum.Period) { state.resetErrors().period() }

    // this is a bit error prone but it's just a sample app...
    reduce { state.resetErrors().digit(action.digit ?: 0) }

}
