package com.genaku.reduce.counter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.genaku.reduce.BaseActivity
import com.genaku.reduce.R
import com.genaku.reduce.databinding.ActivityCounterSimpleBinding
import com.onegravity.knot.activityKnotContext

class CounterSimpleActivity : BaseActivity() {

    private val viewModel: CounterSimpleViewModel by viewModels {
        viewModelFactory { CounterSimpleViewModel(activityKnotContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityCounterSimpleBinding>(this, R.layout.activity_counter_simple)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

}