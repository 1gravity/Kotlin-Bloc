package com.genaku.reduce.sms

import com.onegravity.knot.sample.sms.ErrorUseCase
import com.onegravity.knot.sample.sms.ISmsRepository
import com.onegravity.knot.sample.sms.LoadingUseCase

object DI {

    val repository = object : ISmsRepository {
        override fun checkSms(sms: String): Boolean {
            Thread.sleep(500)
            return sms == "0101"
        }
    }

    private val errorUseCase = ErrorUseCase()
    val loadingUseCase = LoadingUseCase(errorUseCase)

}