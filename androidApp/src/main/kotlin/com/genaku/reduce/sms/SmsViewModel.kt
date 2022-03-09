package com.genaku.reduce.sms

import androidx.lifecycle.ViewModel
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.sample.sms.ErrorUseCase
import com.onegravity.knot.sample.sms.ISmsRepository
import com.onegravity.knot.sample.sms.LoadingUseCase
import com.onegravity.knot.sample.sms.SmsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class SmsViewModel(context: KnotContext): ViewModel() {

    private val useCaseCoroutineScope : CoroutineScope = CoroutineScope(Job())

    private val repository = object : ISmsRepository {
        override fun checkSms(sms: String): Boolean {
            Thread.sleep(500)
            return sms == "0101"
        }
    }

    val loadingUseCase = LoadingUseCase(context, ErrorUseCase(context))
    val smsUseCase = SmsUseCase(context, repository, loadingUseCase)

    override fun onCleared() {
        useCaseCoroutineScope.cancel()
        super.onCleared()
    }
}