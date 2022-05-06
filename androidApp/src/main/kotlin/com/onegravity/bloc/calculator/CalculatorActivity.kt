package com.onegravity.bloc.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.bind
import com.onegravity.bloc.databinding.ActivityCalculatorBinding
import com.onegravity.bloc.viewModel

class CalculatorActivity : AppCompatActivity() {

    private val viewModel by viewModel { CalculatorViewModel(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.viewmodel = viewModel }
    }

}