package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.Cancel
import com.onegravity.bloc.utils.DEFAULT_JOB_ID
import com.onegravity.bloc.utils.JobConfig
import com.onegravity.bloc.utils.SuspendBlock
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Helper class to manage the CoroutineScope and launch internal and "external" jobs
 * (launched by initializers, thunks or reducers).
 */
internal class CoroutineHelper(private val dispatcher: CoroutineDispatcher) {
    private var isStarted = atomic(false)

    private var scope: CoroutineScope? = null
        private set(value) {
            if (value != null) {
                field = value
                runner = CoroutineLauncher(value)
            }
        }

    private var runner: CoroutineLauncher? = null

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
            scope = null
            true
        } else {
            false
        }

    /**
     * Launch a coroutine and execute the suspend block.
     */
    internal fun launch(block: suspend CoroutineScope.() -> Unit) {
        scope?.launch(block = block)
    }

    /**
     * Launch a coroutine and execute the suspend block using a jobConfig.
     */
    internal fun launch(jobConfig: JobConfig?, block: SuspendBlock): Cancel =
        runner?.launch(jobConfig, block) ?: { }

    /**
     * The CoroutineLauncher can cancel previous jobs when a new job is launched with the same id.
     *
     * A CoroutineLauncher is tied to a bloc either to run initializer, thunk or reducer coroutines.
     * It's using one of the three bloc CoroutineScopes -> it's tied to the bloc's lifecycle.
     */
    private class CoroutineLauncher(private val coroutineScope: CoroutineScope) {
        private val mutex = Mutex()
        private val map: MutableMap<String, ArrayDeque<Job>> = mutableMapOf()

        fun launch(jobConfig: JobConfig?, block: SuspendBlock): Cancel {
            val cancelPrevious = jobConfig?.cancelPrevious == true
            val jobId = jobConfig?.jobId ?: DEFAULT_JOB_ID

            val job = coroutineScope.launch {
                mutex.withLock {
                    val queue = map[jobId] ?: ArrayDeque<Job>().also { map[jobId] = it }
                    if (cancelPrevious) {
                        queue.forEach {
                            it.cancelAndJoin()
                        }
                    }
                    val job = launch(block = block)
                    queue.add(job)
                }
            }

            return { job.cancel() }
        }

    }

}
