package com.onegravity.bloc.counter

import android.os.Bundle
import androidx.activity.viewModels
import com.onegravity.bloc.BaseActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.databinding.ActivityCounterSimpleBinding

class CounterActivity : BaseActivity() {

    private val viewModel by viewModels<CounterSimpleViewModel> { factory { CounterSimpleViewModel(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityCounterSimpleBinding>(R.layout.activity_counter_simple) { it.viewmodel = viewModel }
    }

}