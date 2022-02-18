package com.genaku.reduce.sms

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class SmsUseCaseTest : FreeSpec({
    val repository = object : ISmsRepository {
        override fun checkSms(sms: String): Boolean {
            Thread.sleep(500)
            return sms == "0101"
        }
    }

    "test" - {
//        val usecase = SmsUseCase(repository, )
    }

})
