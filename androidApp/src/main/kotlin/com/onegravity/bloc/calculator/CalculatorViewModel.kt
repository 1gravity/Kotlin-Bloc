package com.onegravity.bloc.calculator

import androidx.lifecycle.viewModelScope
import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.R
import com.onegravity.bloc.sample.calculator.Action.*
import com.onegravity.bloc.sample.calculator.bloc
import com.onegravity.bloc.toLiveData

class CalculatorViewModel(context: ActivityBlocContext) : BaseViewModel(context) {

    private val bloc = viewModelContext.bloc()

    val state = bloc.toLiveData(viewModelScope)

    private val view2Action = mapOf(
        R.id.button_clear to Clear,
        R.id.button_add to Add,
        R.id.button_subtract to Subtract,
        R.id.button_multiply to Multiply,
        R.id.button_divide to Divide,
        R.id.button_plus_minus to PlusMinus,
        R.id.button_percentage to Percentage,
        R.id.button_period to Period,
        R.id.button_equals to Equals,
        R.id.button_0 to Digit(0),
        R.id.button_1 to Digit(1),
        R.id.button_2 to Digit(2),
        R.id.button_3 to Digit(3),
        R.id.button_4 to Digit(4),
        R.id.button_5 to Digit(5),
        R.id.button_6 to Digit(6),
        R.id.button_7 to Digit(7),
        R.id.button_8 to Digit(8),
        R.id.button_9 to Digit(9)
    )

    fun button(id: Int) {
        view2Action[id]?.let { bloc.send(it) }
    }

}