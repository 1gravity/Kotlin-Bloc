package com.onegravity.bloc.stock

import android.os.Bundle
import androidx.activity.viewModels
import com.onegravity.bloc.BaseActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.databinding.ActivityCalculatorBinding

class StockActivity : BaseActivity() {

    private val viewModel by viewModels<StockViewModel> { factory { StockViewModel(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.viewmodel = viewModel }
    }

}