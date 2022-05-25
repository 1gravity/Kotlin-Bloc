package com.onegravity.bloc.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger as KermitLogger

// use this and not logger(), logger() is only required to retrieve the platform specific logger
public val logger: Logger by lazy {
    KermitLogger.setTag("bloc")
    KermitLogger.setLogWriters(logger())
    LoggerImpl.i("Logger initialized")
    LoggerImpl
}

public expect fun logger(): LogWriter
