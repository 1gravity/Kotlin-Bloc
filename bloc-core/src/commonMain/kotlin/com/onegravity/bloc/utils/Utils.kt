package com.onegravity.bloc.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger as KermitLogger

val logger: Logger by lazy {
    KermitLogger.setTag("bloc")
    KermitLogger.setLogWriters(logger())
    LoggerImpl
}

expect fun logger(): LogWriter
