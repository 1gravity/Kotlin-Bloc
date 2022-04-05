package com.onegravity.bloc.sample.calculator

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext

enum class ActionEnum(val digit: Int? = null) {
    Clear,
    Add,
    Subtract,
    Multiply,
    Divide,
    PlusMinus,
    Percentage,
    Digit0(0),
    Digit1(1),
    Digit2(2),
    Digit3(3),
    Digit4(4),
    Digit5(5),
    Digit6(6),
    Digit7(7),
    Digit8(8),
    Digit9(9),
    Period,
    Equals
}

/**
 * In this example we're using an Enum as Action. This allows us to bind the layouts directly to the
 * Enum without extra transformation.
 */
fun blocEnum(context: BlocContext) = bloc<State, ActionEnum>(context, State()) {
    fun State.resetErrors() = if (register1.isError() || register2.isError()) State() else this

    reduce(ActionEnum.Clear) { State() }
    reduce(ActionEnum.Equals) { state.resetErrors().equals() }
    reduce(ActionEnum.Clear) { State() }
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
