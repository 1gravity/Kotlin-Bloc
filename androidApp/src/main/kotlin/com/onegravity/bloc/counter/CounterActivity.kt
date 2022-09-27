package com.onegravity.bloc.counter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.bind
import com.onegravity.bloc.databinding.ActivityCounterSimpleBinding

class CounterActivity : AppCompatActivity() {

    private val viewModel: CounterSimpleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityCounterSimpleBinding>(R.layout.activity_counter_simple) {
            it.viewmodel = viewModel
        }
    }

}