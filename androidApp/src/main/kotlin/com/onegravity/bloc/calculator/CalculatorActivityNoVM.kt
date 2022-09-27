package com.onegravity.bloc.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.bind
import com.onegravity.bloc.databinding.ActivityCalculatorNoVmBinding
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.calculator.ActionEnum
import com.onegravity.bloc.toLiveData
import com.onegravity.bloc.sample.calculator.blocEnum as calculatorBloc

class CalculatorActivityNoVM : AppCompatActivity() {

    val bloc by getOrCreate { calculatorBloc(it) }

    // this needs to be lazy because the bloc won't be initialized before onCreate is called
    val state by lazy { toLiveData(bloc) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind<ActivityCalculatorNoVmBinding>(R.layout.activity_calculator_no_vm) {
            it.activity = this
        }
    }

    fun button(action: ActionEnum) {
        bloc.send(action)
    }

}
