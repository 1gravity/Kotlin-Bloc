package com.onegravity.bloc.utils

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger as KermitLogger

/**
 * Platform specific LogWriter, don't call this directly
 */
internal expect fun logWriter(): LogWriter

/**
 * Change this in tests to use the CommonLogger instead of the platform-specific logger
 */
internal fun configureLogger(tag: String = "bloc", usePlatformWriter: Boolean = true) {
    KermitLogger.setTag(tag)

    // use CommonWriter for tests, platform-specific LogWriter otherwise
    val writer = if (usePlatformWriter) logWriter() else CommonWriter()
    KermitLogger.setLogWriters(writer)
}

internal val logger by lazy {
    KermitLogger.setLogWriters(logWriter())
    KermitLogger.withTag("bloc")
    KermitLogger
}
