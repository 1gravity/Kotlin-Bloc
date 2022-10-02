package com.onegravity.bloc.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger

/**
 * Platform specific LogWriter, don't call this directly
 */
internal expect fun logWriter(): LogWriter

// use CommonWriter() for tests, platform-specific logWriter() otherwise
internal var logWriter = logWriter()
    set(value) {
        field = value
        Logger.setLogWriters(value)
    }

internal var tag = "bloc"
    set(value) {
        field = value
        Logger.setTag(tag)
    }

internal val logger by lazy {
    Logger.setLogWriters(logWriter)
    Logger.setTag(tag)
    Logger
}
