package com.genaku.reduce.sms

import com.genaku.reduce.Action
import com.genaku.reduce.Intent
import com.genaku.reduce.State
import com.genaku.reduce.knot

sealed class SmsState : State {
    object InputSms : SmsState()
    object CheckSms : SmsState()
    object SmsConfirmed : SmsState()
    object Exit : SmsState()
}

sealed class SmsIntent : Intent {
    class SendSms(val sms: String) : SmsIntent()
    object Cancel : SmsIntent()
    object WrongSms : SmsIntent()
    object CorrectSms : SmsIntent()
}

sealed class SmsAction : Action {
    class SendSms(val sms: String) : SmsAction()
}