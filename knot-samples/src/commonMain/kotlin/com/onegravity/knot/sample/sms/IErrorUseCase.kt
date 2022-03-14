package com.onegravity.knot.sample.sms

import com.onegravity.bloc.Stream

interface IErrorUseCase {
    val errorState: Stream<ErrorState>

    fun processError(error: IError)
    fun clearError()
}