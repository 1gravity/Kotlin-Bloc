package com.genaku.reduce.sms

import com.genaku.reduce.Intent
import com.genaku.reduce.State

interface IError{
    val msg: String
}

sealed class ErrorState: State {
    object NoError: ErrorState()
    data class Error(val error: IError): ErrorState()
}
sealed class ErrorIntent : Intent {
    data class SetError(val error: IError): ErrorIntent()
    object ClearError: ErrorIntent()
}