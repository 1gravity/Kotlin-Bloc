package com.genaku.reduce

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.onegravity.knot.defaultKnotContext
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory { MainViewModel(defaultKnotContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.d()
        }
        val txt = findViewById<TextView>(R.id.tvText)
        lifecycleScope.launch {
            viewModel.state.collect {
                txt.text = it.name
            }
        }
    }

}