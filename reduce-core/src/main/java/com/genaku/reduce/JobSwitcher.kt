package com.genaku.reduce

import kotlinx.coroutines.CoroutineScope

interface JobSwitcher {
    fun start(scope: CoroutineScope)
    fun stop()
}