package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.loggerConfig
import kotlin.test.BeforeTest

@Suppress("UnnecessaryAbstractClass")
abstract class BaseTestClass {

    @BeforeTest
    fun initialize() {
        loggerConfig = loggerConfig.copy(useCommonWriter = true)
    }

}
