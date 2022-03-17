package com.onegravity.bloc.counter

import android.os.Bundle
import androidx.activity.viewModels
import com.onegravity.bloc.BaseActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.databinding.ActivityCounterReduxBinding

class CounterReduxActivity : BaseActivity() {

    private val viewModel by viewModels<CounterReduxViewModel> { factory { CounterReduxViewModel(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityCounterReduxBinding>(R.layout.activity_counter_redux) { it.viewmodel = viewModel }
    }

}