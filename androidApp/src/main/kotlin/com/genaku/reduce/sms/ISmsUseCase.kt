package com.genaku.reduce.sms

import com.onegravity.knot.*

interface ISmsUseCase {
    val state: Stream<SmsState>

    fun checkSms(sms: String)
    fun cancel()
}