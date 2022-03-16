package com.onegravity.bloc.counter

import android.os.Bundle
import androidx.activity.viewModels
import com.onegravity.bloc.BaseActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.databinding.ActivityCounterReduxBinding
import com.onegravity.knot.activityBlocContext

class CounterReduxActivity : BaseActivity() {

    private val viewModel: CounterReduxViewModel by viewModels {
        viewModelFactory { CounterReduxViewModel(activityBlocContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityCounterReduxBinding>(R.layout.activity_counter_redux) { it.viewmodel = viewModel }
    }

}