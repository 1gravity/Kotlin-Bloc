package com.onegravity.bloc.internal

import co.touchlab.kermit.CommonWriter
import com.onegravity.bloc.utils.logWriter
import kotlin.test.BeforeTest

@Suppress("UnnecessaryAbstractClass")
abstract class BaseTestClass {

    @BeforeTest
    fun initialize() {
        logWriter = CommonWriter()
    }

}
