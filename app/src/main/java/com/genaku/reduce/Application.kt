package com.genaku.reduce

import android.app.Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        PLogger.initPLog(BuildConfig.DEBUG)
    }
}