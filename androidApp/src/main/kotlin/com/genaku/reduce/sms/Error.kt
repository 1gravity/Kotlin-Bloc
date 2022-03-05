//package com.genaku.reduce.sms
//
//import com.onegravity.knot.*
//
//interface IError{
//    val msg: String
//}
//
//sealed class ErrorState: State {
//    object NoError: ErrorState()
//    data class Error(val error: IError): ErrorState()
//}
//sealed class ErrorIntent {
//    data class SetError(val error: IError): ErrorIntent()
//    object ClearError: ErrorIntent()
//}