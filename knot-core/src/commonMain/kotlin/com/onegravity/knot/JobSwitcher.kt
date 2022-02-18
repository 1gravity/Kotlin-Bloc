package com.onegravity.knot

import kotlinx.coroutines.CoroutineScope

interface JobSwitcher {
    fun start(coroutineScope: CoroutineScope)
    fun stop()
}