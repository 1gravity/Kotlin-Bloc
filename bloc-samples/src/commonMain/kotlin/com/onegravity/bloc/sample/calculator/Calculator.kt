package com.onegravity.bloc.sample.calculator

import com.github.michaelbull.result.runCatching
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.calculator.State.Companion.mapToState

sealed class Action {
    object Clear : Action()
    object Add : Action()
    object Subtract : Action()
    object Multiply : Action()
    object Divide : Action()
    object PlusMinus : Action()
    object Percentage : Action()
    data class Digit(val digit: Int) : Action()
    object Period : Action()
    object Equals : Action()
}

/**
 * In this example we're using a sealed class as Action.
 * This way we can use a data class for the digits (less reduce code) but we need to map the view
 * clicks to Action (typically in the ViewModel).
 */
fun bloc(context: BlocContext) = bloc<State, Action>(context, State()) {
    fun State.resetErrors() = if (register1.isError() || register2.isError()) State() else this

    /**
     * We can either define reducers per action...
     */
    reduce<Action.Equals> { state.resetErrors().equals() }
    reduce<Action.Clear> { State() }
    reduce<Action.Add> { state.resetErrors().apply(Operator.Add) }
    reduce<Action.Subtract> { state.resetErrors().apply(Operator.Subtract) }
    reduce<Action.Multiply> { state.resetErrors().apply(Operator.Multiply) }
    reduce<Action.Divide> { state.resetErrors().apply(Operator.Divide) }
    reduce<Action.PlusMinus> { state.resetErrors().plusMinus() }
    reduce<Action.Percentage> { state.resetErrors().percentage() }
    reduce<Action.Period> { state.resetErrors().period() }
    reduce<Action.Digit> { state.resetErrors().digit(action.digit) }

    /**
     * ...or use a single reducer for multiple actions:
     */
//    reduce {
//        try {
//            val newState = state.resetErrors()
//            when (action) {
//                Action.Clear -> State()
//                Action.Equals -> newState.equals()
//                Action.Add -> newState.apply(Operator.Add)
//                Action.Subtract -> newState.apply(Operator.Subtract)
//                Action.Multiply -> newState.apply(Operator.Multiply)
//                Action.Divide -> newState.apply(Operator.Divide)
//                Action.PlusMinus -> newState.plusMinus()
//                Action.Percentage -> newState.percentage()
//                Action.Period -> newState.period()
//                is Action.Digit -> newState.digit(action.digit)
//            }
//        } catch (ex: Exception) {
//            State.error(ex.message)
//        }
//    }
}

internal fun State.apply(op: Operator) = runCatching {
    val state = if (register1.isNotEmpty() && register2.isNotEmpty()) equals() else this
    if (state.register1.isEmpty())
        state
    else state.copy(
        register1 = Register(),
        register2 = if (state.register1.isNotEmpty()) state.register1 else state.register2,
        operator = op
    )
}.mapToState()

internal fun State.plusMinus() = copy(register1 = register1.plusMinus())

internal fun State.period() = copy(register1 = register1.appendPeriod())

internal fun State.digit(digit: Int) = copy(register1 = register1.appendDigit(digit))

internal fun State.percentage() =
    if (register1.isEmpty()) this else copy(register1 = register1 / Register("100"))

internal fun State.equals() = runCatching {
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