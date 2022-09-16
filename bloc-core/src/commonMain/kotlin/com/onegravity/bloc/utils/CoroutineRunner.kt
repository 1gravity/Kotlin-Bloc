package com.onegravity.bloc.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// TODO use a Queue instead of a Map
internal class CoroutineRunner {
    private val map: MutableMap<String, Job> = HashMap()

    internal fun runSingle(
        jobConfig: JobConfig,
        coroutineScope: CoroutineScope,
        block: Coroutine
    ) {
        map[jobConfig.jobId]
            ?.run { cancel() }
            ?:run {
                val job = coroutineScope.launch { block() }
                map[jobConfig.jobId] = job
            }
    }
}