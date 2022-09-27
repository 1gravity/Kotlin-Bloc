package com.onegravity.bloc.utils

/**
 * The JobConfig defines how a coroutine/job is executed.
 * @param cancelPrevious If false (default) a coroutine is simply launched, no additional checks.
 *                       If true, all previous jobs that were started with the same jobId, will be
 *                       cancelled and the coroutine is suspended till all jobs have finished
 *                       (cancelAndJoin).
 * @param jobId if cancelPrevious is true, then the jobId can be used to group different jobs
 *              together to make sure only one of them is run at a time. The jobId defaults to
 *              "DefaultJobId".
 */
public data class JobConfig(val cancelPrevious: Boolean = false, val jobId: String = DEFAULT_JOB_ID)

internal const val DEFAULT_JOB_ID = "DefaultJobId"
