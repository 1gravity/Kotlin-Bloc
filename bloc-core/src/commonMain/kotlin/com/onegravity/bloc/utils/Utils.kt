package com.onegravity.bloc.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger as KermitLogger

// we can either use this or the logger() function below, the result is identical
val logger: Logger by lazy {
    KermitLogger.setTag("bloc")
    KermitLogger.setLogWriters(logger())
    LoggerImpl
}

expect fun logger(): LogWriter
