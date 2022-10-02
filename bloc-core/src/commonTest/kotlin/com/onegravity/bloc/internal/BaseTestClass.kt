package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.configureLogger
import kotlin.test.BeforeTest

@Suppress("UnnecessaryAbstractClass")
abstract class BaseTestClass {

    @BeforeTest
    fun initialize() {
        configureLogger(usePlatformWriter = false)
    }

}
