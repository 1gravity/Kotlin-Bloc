package com.onegravity.bloc

import androidx.multidex.MultiDexApplication
import com.onegravity.bloc.util.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.dsl.module

class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@Application)
            androidLogger(Level.ERROR)
        }
    }

}
