package com.onegravity.knot.sample.sms

import com.onegravity.bloc.Stream

interface ISmsUseCase {
    val state: Stream<SmsState>

    fun checkSms(sms: String)
    fun cancel()
}