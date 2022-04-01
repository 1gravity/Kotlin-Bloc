package com.onegravity.bloc.counter

import android.os.Bundle
import com.onegravity.bloc.viewModel
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.bind
import com.onegravity.bloc.databinding.ActivityCounterReduxBinding

class CounterReduxActivity : AppCompatActivity() {

    private val viewModel by viewModel { CounterReduxViewModel(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityCounterReduxBinding>(R.layout.activity_counter_redux) { it.viewmodel = viewModel }
    }

}