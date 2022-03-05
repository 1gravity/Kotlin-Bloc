package com.genaku.reduce.sms

import com.onegravity.knot.Stream

interface IErrorUseCase {
    val errorState: Stream<ErrorState>

    fun processError(error: IError)
    fun clearError()
}