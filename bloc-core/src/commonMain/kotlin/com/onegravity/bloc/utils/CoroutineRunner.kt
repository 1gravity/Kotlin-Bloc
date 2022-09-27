package com.onegravity.bloc.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * The CoroutineRunner is tied to a bloc either to run initializer, thunk or reducer coroutines.
 * It's using one of the three bloc CoroutineScopes -> it's tied to the bloc's lifecycle.
 *
 * Unfortunately the CoroutineRunner needs to be public because it's exposed by public functions
 * (InitializerContext, ThunkContext, ReducerContext etc.).
 */
public class CoroutineRunner(private val coroutineScope: CoroutineScope) {
    private val mutex = Mutex()
    private val map: MutableMap<String, ArrayDeque<Job>> = mutableMapOf()

    internal fun run(
        jobConfig: JobConfig?,
        block: CoroutineBlock
    ) {
        val cancelPrevious = jobConfig?.cancelPrevious == true
        val jobId = jobConfig?.jobId ?: DEFAULT_JOB_ID

        coroutineScope.launch {
            mutex.withLock {
                val queue = map[jobId] ?: ArrayDeque<Job>().also { map[jobId] = it }
                if (cancelPrevious) {
                    queue.forEach {
                        it.cancelAndJoin()
                    }
                }
                val job = coroutineScope.launch(block = block)
                queue.add(job)
            }
        }
    }

}
