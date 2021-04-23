package com.genaku.reduce.sms

import app.cash.turbine.test
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ErrorUseCaseTest : FreeSpec({
    val coroutineDispatcher = TestCoroutineDispatcher()
    val useCase = ErrorUseCase(coroutineDispatcher)
    val error = object : IError {
        override val msg: String
            get() = "error"
    }
    coroutineDispatcher.runBlockingTest {
        "initial state should be NoError" {
            useCase.start(this)
            useCase.errorState.test {
                expectItem() shouldBe ErrorState.NoError
                cancelAndIgnoreRemainingEvents()
            }
            useCase.stop()
        }
        "process error should emit error state" - {
            useCase.start(this)
            useCase.processError(error)
            useCase.errorState.test {
                expectItem() shouldBe ErrorState.Error(error)
                cancelAndIgnoreRemainingEvents()
            }
            "clearError should clear previous error to NoError" {
                useCase.clearError()
                useCase.errorState.test {
                    expectItem() shouldBe ErrorState.NoError
                    cancelAndIgnoreRemainingEvents()
                }
            }
            useCase.stop()
        }
    }
})
