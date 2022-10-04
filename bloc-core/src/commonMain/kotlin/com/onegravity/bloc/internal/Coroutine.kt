package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.CoroutineRunner
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Helper class to manage CoroutineScope and CoroutineRunner
 */
internal class Coroutine(private val dispatcher: CoroutineDispatcher) {
    private var isStarted = atomic(false)

    internal var scope: CoroutineScope? = null
        private set(value) {
            if (value != null) {
                field = value
                runner = CoroutineRunner(value)
            }
        }

    internal var runner: CoroutineRunner? = null
        private set

    /**
     * Reentrant, can be called multiple times but will start only once.
     *
     * @return True if starting was successful (not started yet), False otherwise (already started)
     */
    internal fun onStart(): Boolean =
        if (isStarted.compareAndSet(expect = false, update = true)) {
            scope = CoroutineScope(SupervisorJob() + dispatcher)
            true
        } else {
            false
        }

    /**
     * Reentrant, can be called multiple times but will stop only once.
     *
     * @return True if stopping was successful (was started), False otherwise (already stopped)
     */
    internal fun onStop(): Boolean =
        if (isStarted.compareAndSet(expect = true, update = false)) {
            scope?.cancel()
            true
        } else {
            false
        }
}
