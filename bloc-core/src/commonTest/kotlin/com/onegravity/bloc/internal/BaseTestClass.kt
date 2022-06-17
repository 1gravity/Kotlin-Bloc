package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.loggerConfig
import kotlin.test.BeforeTest

abstract class BaseTestClass {

    @BeforeTest
    fun initialize() {
        loggerConfig = loggerConfig.copy(useCommonWriter = true)
    }

}
