package com.onegravity.bloc.sample.calculator

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
