package com.genaku.reduce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.mym.plog.PLog

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.d()
        }
        val txt = findViewById<TextView>(R.id.tvText)
        lifecycleScope.launch {
            viewModel.state.collect {
                PLog.d("state ${it.name}")
                txt.text = it.name
            }
        }
    }
}