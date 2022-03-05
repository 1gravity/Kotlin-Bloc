package com.genaku.reduce.station

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.genaku.reduce.R
import kotlinx.coroutines.launch

class StationActivity : AppCompatActivity() {

    private val viewModel: StationViewModel by viewModels()

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