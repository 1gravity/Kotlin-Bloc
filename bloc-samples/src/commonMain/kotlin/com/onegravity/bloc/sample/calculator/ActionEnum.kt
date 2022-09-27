package com.onegravity.bloc.sample.calculator

@Suppress("MagicNumber")
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
