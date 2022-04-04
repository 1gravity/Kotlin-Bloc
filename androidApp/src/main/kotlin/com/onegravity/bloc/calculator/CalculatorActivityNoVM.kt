package com.onegravity.bloc.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.bind
import com.onegravity.bloc.databinding.ActivityCalculatorNoVmBinding
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.calculator.Action
import com.onegravity.bloc.toLiveData
import com.onegravity.bloc.sample.calculator.bloc as calculatorBloc

class CalculatorActivityNoVM : AppCompatActivity() {

    val bloc by getOrCreate { it.calculatorBloc() }

    // this needs to be lazy because the bloc won't be initialized before onCreate is called
    val state by lazy { toLiveData(bloc) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind<ActivityCalculatorNoVmBinding>(R.layout.activity_calculator_no_vm) { it.activity = this }
    }

    private val view2Action = mapOf(
        R.id.button_clear to Action.Clear,
        R.id.button_add to Action.Add,
        R.id.button_subtract to Action.Subtract,
        R.id.button_multiply to Action.Multiply,
        R.id.button_divide to Action.Divide,
        R.id.button_plus_minus to Action.PlusMinus,
        R.id.button_percentage to Action.Percentage,
        R.id.button_period to Action.Period,
        R.id.button_equals to Action.Equals,
        R.id.button_0 to Action.Digit(0),
        R.id.button_1 to Action.Digit(1),
        R.id.button_2 to Action.Digit(2),
        R.id.button_3 to Action.Digit(3),
        R.id.button_4 to Action.Digit(4),
        R.id.button_5 to Action.Digit(5),
        R.id.button_6 to Action.Digit(6),
        R.id.button_7 to Action.Digit(7),
        R.id.button_8 to Action.Digit(8),
        R.id.button_9 to Action.Digit(9)
    )

    fun button(id: Int) {
        view2Action[id]?.let { bloc.send(it) }
    }

}