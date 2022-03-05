//package com.genaku.reduce.sms
//
//import android.os.Bundle
//import android.view.View
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import by.kirich1409.viewbindingdelegate.viewBinding
//import com.genaku.reduce.R
//import com.genaku.reduce.databinding.ActivitySmsBinding
//import org.mym.plog.PLog
//
//class SmsActivity : AppCompatActivity(R.layout.activity_sms) {
//
//    private val viewBinding by viewBinding(ActivitySmsBinding::bind)
//    private val viewModel: SmsViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        observeStates()
//        setupButtons()
//    }
//
//    private fun observeStates() {
//        observeState(viewModel.smsUseCase.state) {
//            when (it) {
//                SmsState.Exit -> finish()
//                SmsState.SmsConfirmed -> viewBinding.tvSuccess.visibility = View.VISIBLE
//                else -> {
//                }
//            }
//        }
//        observeState(viewModel.loadingUseCase.loadingState) {
//            progress(it == LoadingState.Active)
//        }
//        observeState(viewModel.loadingUseCase.errorState) {
//            PLog.d("error: $it")
//            error(it != ErrorState.NoError)
//        }
//    }
//
//    private fun setupButtons() {
//        with(viewBinding) {
//            button.setOnClickListener {
//                viewModel.smsUseCase.checkSms(edCode.text.toString())
//            }
//            btnCancel.setOnClickListener {
//                viewModel.smsUseCase.cancel()
//            }
//        }
//    }
//
//    private fun progress(visible: Boolean) {
//        viewBinding.tvProgress.visibility = if (visible) View.VISIBLE else View.GONE
//    }
//
//    private fun error(visible: Boolean) {
//        PLog.d("error $visible")
//        viewBinding.tvError.visibility = if (visible) View.VISIBLE else View.GONE
//    }
//}