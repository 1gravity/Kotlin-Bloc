package com.genaku.reduce.sms

import com.genaku.reduce.JobSwitcher
import kotlinx.coroutines.flow.StateFlow

interface ISmsUseCase : JobSwitcher {
    val state: StateFlow<SmsState>

    fun checkSms(sms: String)
    fun cancel()
}