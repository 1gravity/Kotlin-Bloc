package com.onegravity.knot.sample.sms

interface IError{
    val msg: String
}

sealed class ErrorState {
    object NoError: ErrorState()
    data class Error(val error: IError): ErrorState()
}
sealed class ErrorEvent {
    data class SetError(val error: IError): ErrorEvent()
    object ClearError: ErrorEvent()
}