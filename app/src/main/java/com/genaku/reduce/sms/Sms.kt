package com.genaku.reduce.sms

import com.genaku.reduce.StateIntent
import com.genaku.reduce.State

sealed class SmsState : State {
    object InputSms : SmsState()
    object CheckSms : SmsState()
    object SmsConfirmed : SmsState()
    object Exit : SmsState()
}

sealed class SmsIntent : StateIntent {
    class SendSms(val sms: String) : SmsIntent()
    object Cancel : SmsIntent()
    object WrongSms : SmsIntent()
    object CorrectSms : SmsIntent()
}