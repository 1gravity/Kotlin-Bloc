package com.genaku.reduce.sms

import com.genaku.reduce.StateIntent
import com.genaku.reduce.State

interface IError{
    val msg: String
}

sealed class ErrorState: State {
    object NoError: ErrorState()
    data class Error(val error: IError): ErrorState()
}
sealed class ErrorIntent : StateIntent {
    data class SetError(val error: IError): ErrorIntent()
    object ClearError: ErrorIntent()
}