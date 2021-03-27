package com.genaku.reduce

import org.mym.plog.DebugPrinter
import org.mym.plog.PLog
import org.mym.plog.config.PLogConfig

object PLogger {
    fun initPLog(isDebug: Boolean) {
        PLog.init(
            PLogConfig.Builder()
                .forceConcatGlobalTag(false)
                .needLineNumber(true)
                .useAutoTag(true)
                .emptyMsg("")
                .needThreadInfo(true)
                .build()
        )
        PLog.prepare(DebugPrinter(isDebug)) // all logs will be automatically disabled on release version
    }
}