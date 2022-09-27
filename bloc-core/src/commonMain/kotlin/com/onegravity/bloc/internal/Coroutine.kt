package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.CoroutineRunner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Helper class to manage CoroutineScope and CoroutineRunner
 */
internal class Coroutine(private val dispatcher: CoroutineDispatcher) {
    internal var scope: CoroutineScope? = null
        private set(value) {
            if (value != null) {
                field = value
                runner = CoroutineRunner(value)
            }
        }

    internal var runner: CoroutineRunner? = null
        private set

    internal fun onStart() {
        scope = CoroutineScope(SupervisorJob() + dispatcher)
    }

    internal fun onStop() {
        scope?.cancel()
    }
}
