package com.onegravity.bloc.utils

import kotlinx.coroutines.launch

/**
 * TODO document
 */
public data class JobConfig(val runSingle: Boolean = false, val jobId: String = "DefaultJobId")

/**
 * InitializerContext extension function to encapsulate the CoroutineScope
 * (initializers shouldn't manipulate the CoroutineScope)
 */
@BlocDSL
public fun <State, Action> InitializerContext<State, Action>.launch(block: Coroutine) {
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
    block: Coroutine
) {
    when (jobConfig?.runSingle) {
        true -> runSingle(jobConfig, block)
        else -> coroutineScope.launch { block() }
    }
}

@BlocDSL
public fun <State, Action, A : Action> ThunkContext<State, Action, A>.launch(block: Coroutine) {
    launch(null, block)
}

/**
 * ThunkContextNoAction extension function (see [ThunkContext])
 */
@BlocDSL
public fun <State, Action> ThunkContextNoAction<State, Action>.launch(
    jobConfig: JobConfig? = null,
    block: Coroutine
) {
    when (jobConfig?.runSingle) {
        true -> runSingle(jobConfig, block)
        else -> coroutineScope.launch { block() }
    }
}

@BlocDSL
public fun <State, Action> ThunkContextNoAction<State, Action>.launch(block: Coroutine) {
    launch(null, block)
}

/**
 * ReducerContext extension function to:
 * 1. encapsulate the CoroutineScope (reducers shouldn't manipulate the CoroutineScope)
 * 2. add extra functionality like auto-cancel of previously started jobs
 */
@BlocDSL
public fun <State, Action> ReducerContext<State, Action>.launch(
    jobConfig: JobConfig? = null,
    block: Coroutine
) {
    when (jobConfig?.runSingle) {
        true -> runSingle(jobConfig, block)
        else -> coroutineScope.launch { block() }
    }
}

@BlocDSL
public fun <State, Action> ReducerContext<State, Action>.launch(
    block: Coroutine
) {
    launch(null) { block() }
}

/**
 * ReducerContextNoAction extension function (see [ReducerContext])
 */
@BlocDSL
public fun <State> ReducerContextNoAction<State>.launch(
    jobConfig: JobConfig? = null,
    block: Coroutine
) {
    when (jobConfig?.runSingle) {
        true -> runSingle(jobConfig, block)
        else -> coroutineScope.launch { block() }
    }
}

@BlocDSL
public fun <State> ReducerContextNoAction<State>.launch(block: Coroutine) {
    launch(null, block)
}
