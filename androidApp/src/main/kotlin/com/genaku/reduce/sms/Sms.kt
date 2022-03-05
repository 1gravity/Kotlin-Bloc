package com.genaku.reduce.sms

sealed class SmsState {
    object InputSms : SmsState()
    object CheckSms : SmsState()
    object SmsConfirmed : SmsState()
    object Exit : SmsState()
}

sealed class SmsEvent {
    class SendSms(val sms: String) : SmsEvent()
    object Cancel : SmsEvent()
    object WrongSms : SmsEvent()
    object CorrectSms : SmsEvent()
}