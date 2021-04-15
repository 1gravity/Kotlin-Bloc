package com.genaku.reduce.sms

object DI {

    private val repository = object : ISmsRepository {
        override fun checkSms(sms: String): Boolean {
            Thread.sleep(500)
            return sms == "0101"
        }
    }

    private val errorUseCase = ErrorUseCase()
    private val loadingUseCase = LoadingUseCase(errorUseCase)

    val useCase = SmsUseCase(repository, loadingUseCase)

}