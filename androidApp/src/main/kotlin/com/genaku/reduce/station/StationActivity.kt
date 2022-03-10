package com.genaku.reduce.station

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.genaku.reduce.BaseActivity
import com.genaku.reduce.R
import com.onegravity.knot.defaultKnotContext
import kotlinx.coroutines.launch

class StationActivity : BaseActivity() {

    private val viewModel: StationViewModel by viewModels {
        viewModelFactory { StationViewModel(defaultKnotContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.switch()
        }
        val txt = findViewById<TextView>(R.id.tvText)
        lifecycleScope.launch {
            viewModel.state.collect {
                txt.text = it.getValue()
            }
        }
    }
}