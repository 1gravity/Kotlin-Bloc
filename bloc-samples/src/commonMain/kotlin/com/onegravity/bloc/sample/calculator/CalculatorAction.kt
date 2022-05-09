package com.onegravity.bloc.sample.calculator

sealed class CalculatorAction {
    object Clear : CalculatorAction()
    object Add : CalculatorAction()
    object Subtract : CalculatorAction()
    object Multiply : CalculatorAction()
    object Divide : CalculatorAction()
    object PlusMinus : CalculatorAction()
    object Percentage : CalculatorAction()
    data class Digit(val digit: Int) : CalculatorAction()
    object Period : CalculatorAction()
    object Equals : CalculatorAction()
}
