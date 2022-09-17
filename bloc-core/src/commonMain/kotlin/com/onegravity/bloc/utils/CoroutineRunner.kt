package com.onegravity.bloc.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val DefaultJobId = "DefaultJobId"

/**
 * TODO document
 */
public data class JobConfig(val cancelPrevious: Boolean = false, val jobId: String = DefaultJobId)

// TODO use a Queue instead of a Map
/**
 * Unfortunately this needs to be public....
 */
public class CoroutineRunner(private val coroutineScope: CoroutineScope) {
    private val map: MutableMap<String, Job> = HashMap()

    internal fun run(
        jobConfig: JobConfig?,
        block: CoroutineBlock
    ) {
        val cancelPrevious = jobConfig?.cancelPrevious == true
        val jobId = jobConfig?.jobId ?: DefaultJobId
        if (cancelPrevious) {
            map[jobId]?.cancel()
        }
        map[jobId] = coroutineScope.launch { block() }
    }

}