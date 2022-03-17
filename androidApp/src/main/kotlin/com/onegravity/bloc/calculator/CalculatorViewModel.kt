package com.onegravity.bloc.calculator

import androidx.lifecycle.viewModelScope
import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.R
import com.onegravity.bloc.sample.calculator.bloc
import com.onegravity.bloc.sample.calculator.Action.*
import com.onegravity.bloc.toLiveData

class CalculatorViewModel(context: ActivityBlocContext) : BaseViewModel(context) {

    private val bloc = bloc(viewModelContext)

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
    )

    fun button(id: Int) {
        view2Action[id]?.let { bloc.emit(it) }
    }

    fun digit(digit: Int) {
        bloc.emit(Digit(digit))
    }

}