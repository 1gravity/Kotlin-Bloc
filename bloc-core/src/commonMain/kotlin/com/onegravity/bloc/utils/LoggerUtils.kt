package com.onegravity.bloc.utils

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger as KermitLogger

/**
 * Change this in tests to use the CommonLogger instead of the platform-specific logger
 */
public var loggerConfig: LoggerConfig = LoggerConfig()

/**
 * Platform specific LogWriter, don't call this directly
 */
public expect fun logger(): LogWriter

// use this and not logger(), logger() is only required to retrieve the platform specific logger
public val logger: Logger by lazy {
    KermitLogger.setTag(loggerConfig.tag)

    // use CommonWriter for tests, platform-specific LogWriter otherwise
    val writer = if (loggerConfig.useCommonWriter) CommonWriter() else logger()
    KermitLogger.setLogWriters(writer)

    LoggerImpl.i("Logger initialized")
    LoggerImpl
}
