package com.genaku.reduce.sms

import com.genaku.reduce.BaseViewModel
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.sample.sms.ErrorUseCase
import com.onegravity.knot.sample.sms.ISmsRepository
import com.onegravity.knot.sample.sms.LoadingUseCase
import com.onegravity.knot.sample.sms.SmsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class SmsViewModel(context: ActivityKnotContext): BaseViewModel(context) {

    private val useCaseCoroutineScope : CoroutineScope = CoroutineScope(Job())

    private val repository = object : ISmsRepository {
        override fun checkSms(sms: String): Boolean {
            Thread.sleep(500)
            return sms == "0101"
        }
    }

    val loadingUseCase = LoadingUseCase(viewModelContext, ErrorUseCase(viewModelContext))
    val smsUseCase = SmsUseCase(viewModelContext, repository, loadingUseCase)

    override fun onCleared() {
        useCaseCoroutineScope.cancel()
        super.onCleared()
    }
}