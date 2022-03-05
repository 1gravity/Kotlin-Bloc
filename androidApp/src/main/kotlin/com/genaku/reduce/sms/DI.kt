package com.genaku.reduce.sms

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