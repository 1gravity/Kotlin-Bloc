package com.onegravity.bloc.calculator

import android.os.Bundle
import androidx.activity.viewModels
import com.onegravity.bloc.BaseActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.databinding.ActivityCalculatorBinding

class CalculatorActivity : BaseActivity() {

    private val viewModel by viewModels<CalculatorViewModel> { factory { CalculatorViewModel(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.viewmodel = viewModel }
    }

}