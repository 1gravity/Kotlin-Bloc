package com.onegravity.bloc.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * InitializerContext extension function to encapsulate the CoroutineScope
 * (initializers shouldn't manipulate the CoroutineScope)
 */
@BlocDSL
public fun <State, Action> InitializerContext<State, Action>.launch(
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch { block() }
}

/**
 * ThunkContext extension function to:
 * 1. encapsulate the CoroutineScope (thunks shouldn't manipulate the CoroutineScope)
 * 2. add extra functionality like auto-cancel of previously started jobs
 */
@BlocDSL
public fun <State, Action, A : Action> ThunkContext<State, Action, A>.launch(
    jobConfig: JobConfig? = null,
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch { block() }
}

/**
 * ThunkContextNoAction extension function (see [ThunkContext])
 */
@BlocDSL
public fun <State, Action> ThunkContextNoAction<State, Action>.launch(
    jobConfig: JobConfig? = null,
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch { block() }
}

/**
 * ReducerContext extension function to:
 * 1. encapsulate the CoroutineScope (reducers shouldn't manipulate the CoroutineScope)
 * 2. add extra functionality like auto-cancel of previously started jobs
 */
@BlocDSL
public fun <State, Action> ReducerContext<State, Action>.launch(
    jobConfig: JobConfig? = null,
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch { block() }
}

public data class JobConfig(val runSingle: Boolean = false, val jobId: String = "DefaultJobId")

/**
 * ReducerContextNoAction extension function (see [ReducerContext])
 */
@BlocDSL
public fun <State> ReducerContextNoAction<State>.launch(
    jobConfig: JobConfig? = null,
    block: suspend CoroutineScope.() -> Unit
) {
    when (jobConfig?.runSingle) {
        true -> runSingle(jobConfig, block)
        else -> coroutineScope.launch { block() }
    }
}
