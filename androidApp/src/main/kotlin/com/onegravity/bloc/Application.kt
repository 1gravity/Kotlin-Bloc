package com.onegravity.bloc

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class Application: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Application)
            modules(listOf(mainModule))
        }
    }

    private val mainModule = module {
        // TBD
    }

}