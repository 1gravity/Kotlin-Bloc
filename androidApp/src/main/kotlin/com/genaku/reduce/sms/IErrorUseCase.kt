package com.genaku.reduce.sms

import kotlinx.coroutines.flow.StateFlow

interface IErrorUseCase {
    val errorState: StateFlow<ErrorState>

    fun processError(error: IError)
    fun clearError()
}