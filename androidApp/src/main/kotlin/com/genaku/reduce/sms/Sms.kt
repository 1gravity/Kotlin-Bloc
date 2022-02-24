package com.genaku.reduce.sms

import com.onegravity.knot.*

sealed class SmsState : State {
    object InputSms : SmsState()
    object CheckSms : SmsState()
    object SmsConfirmed : SmsState()
    object Exit : SmsState()
}

sealed class SmsIntent {
    class SendSms(val sms: String) : SmsIntent()
    object Cancel : SmsIntent()
    object WrongSms : SmsIntent()
    object CorrectSms : SmsIntent()
}