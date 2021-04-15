package com.genaku.reduce.sms

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.genaku.reduce.R
import com.genaku.reduce.databinding.ActivitySmsBinding
import org.mym.plog.PLog

class SmsActivity : AppCompatActivity(R.layout.activity_sms) {

    private val smsUseCase = DI.useCase

    private val viewBinding by viewBinding(ActivitySmsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeStates()
        setupButtons()
        smsUseCase.start(lifecycleScope)
    }

    private fun observeStates() {
        observeState(smsUseCase.state) {
            if (it == SmsState.Exit) finish()
        }
        observeState(smsUseCase.loadingState) {
            progress(it == LoadingState.Active)
        }
        observeState(smsUseCase.errorState) {
            PLog.d("error: $it")
            error(it != ErrorState.NoError)
        }
    }

    private fun setupButtons() {
        with(viewBinding) {
            button.setOnClickListener {
                smsUseCase.checkSms(viewBinding.edCode.text.toString())
            }
            btnCancel.setOnClickListener {
                smsUseCase.cancel()
            }
        }
    }

    private fun progress(visible: Boolean) {
        viewBinding.tvProgress.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun error(visible: Boolean) {
        PLog.d("error $visible")
        viewBinding.tvError.visibility = if (visible) View.VISIBLE else View.GONE
    }
}