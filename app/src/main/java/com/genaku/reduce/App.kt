package com.genaku.reduce

import android.app.Application
import org.mym.plog.DebugPrinter
import org.mym.plog.PLog
import org.mym.plog.config.PLogConfig

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        PLog.init(
            PLogConfig.Builder()
                .forceConcatGlobalTag(false)
                .needLineNumber(true)
                .useAutoTag(true)
                .emptyMsg("")
                .needThreadInfo(true)
                .build()
        )
        PLog.prepare(DebugPrinter(BuildConfig.DEBUG))
    }
}