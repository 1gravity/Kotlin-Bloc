package com.genaku.reduce

import kotlinx.coroutines.CoroutineScope

interface JobSwitcher {
    fun start(coroutineScope: CoroutineScope)
    fun stop()
}