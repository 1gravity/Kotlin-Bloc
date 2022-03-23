package com.onegravity.bloc.counter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.bind
import com.onegravity.bloc.databinding.ActivityCounterSimpleBinding
import com.onegravity.bloc.factory

class CounterActivity : AppCompatActivity() {

    private val viewModel by viewModels<CounterSimpleViewModel> { factory { CounterSimpleViewModel(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityCounterSimpleBinding>(R.layout.activity_counter_simple) { it.viewmodel = viewModel }
    }

}