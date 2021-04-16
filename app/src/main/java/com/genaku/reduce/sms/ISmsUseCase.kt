package com.genaku.reduce.sms

import kotlinx.coroutines.flow.StateFlow

interface ISmsUseCase : IStateUseCase {
    val state: StateFlow<SmsState>

    fun checkSms(sms: String)
    fun cancel()
}