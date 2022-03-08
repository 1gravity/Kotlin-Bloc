package com.onegravity.knot

import kotlinx.coroutines.CoroutineScope

/**
 * TODO replace this with a Lifecycle aware implementation
 */
interface JobSwitcher {
    fun start(coroutineScope: CoroutineScope)
    fun stop()
}