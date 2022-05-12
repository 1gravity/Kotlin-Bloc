package com.onegravity.bloc.sample.calculator

import com.github.michaelbull.result.runCatching
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.calculator.CalculatorAction.*

/**
 * In this example we're using a sealed class as Action.
 * This way we can use a data class for the digits (less reduce code) but we need to map the view
 * clicks to Action (typically in the ViewModel).
 */
fun bloc(context: BlocContext) = bloc<CalculatorState, CalculatorAction>(context, CalculatorState()) {
    fun CalculatorState.resetErrors() =
        if (register1.isError() || register2.isError()) CalculatorState() else this

    /**
     * We can either define reducers per action...
     */
    reduce<Equals> { state.resetErrors().equals() }
    reduce<Clear> { CalculatorState() }
    reduce<Add> { state.resetErrors().apply(Operator.Add) }
    reduce<Subtract> { state.resetErrors().apply(Operator.Subtract) }
    reduce<Multiply> { state.resetErrors().apply(Operator.Multiply) }
    reduce<Divide> { state.resetErrors().apply(Operator.Divide) }
    reduce<PlusMinus> { state.resetErrors().plusMinus() }
    reduce<Percentage> { state.resetErrors().percentage() }
    reduce<Period> { state.resetErrors().period() }
    reduce<Digit> { state.resetErrors().digit(action.digit) }

    /**
     * ...or use a single reducer for multiple actions:
     */
//    reduce {
//        try {
//            val newState = state.resetErrors()
//            when (action) {
//                Clear -> CalculatorState()
//                Equals -> newState.equals()
//                Add -> newState.apply(Operator.Add)
//                Subtract -> newState.apply(Operator.Subtract)
//                Multiply -> newState.apply(Operator.Multiply)
//                Divide -> newState.apply(Operator.Divide)
//                PlusMinus -> newState.plusMinus()
//                Percentage -> newState.percentage()
//                Period -> newState.period()
//                is Digit -> newState.digit(action.digit)
//            }
//        } catch (ex: Exception) {
//            CalculatorState.error(ex.message)
//        }
//    }
}

internal fun CalculatorState.apply(op: Operator): CalculatorState = runCatching {
    val state = if (register1.isNotEmpty() && register2.isNotEmpty()) equals() else this
    if (state.register1.isEmpty())
        state
    else state.copy(
        register1 = Register(),
        register2 = if (state.register1.isNotEmpty()) state.register1 else state.register2,
        operator = op
    )
}.mapToState()

internal fun CalculatorState.plusMinus() = copy(register1 = register1.plusMinus())

internal fun CalculatorState.period() = copy(register1 = register1.appendPeriod())

internal fun CalculatorState.digit(digit: Int) = copy(register1 = register1.appendDigit(digit))

internal fun CalculatorState.percentage() =
    if (register1.isEmpty()) this else copy(register1 = register1 / Register("100"))

internal fun CalculatorState.equals(): CalculatorState = runCatching {
    if (register2.isEmpty()) return this
    val reg1 = when (operator) {
        Operator.Add -> register2 + register1
        Operator.Subtract -> register2 - register1
        Operator.Multiply -> register2 * register1
        Operator.Divide -> register2 / register1
        else -> register1
    }
    copy(register1 = reg1, register2 = Register(), operator = null)
}.mapToState()
