package com.onegravity.bloc.internal

import com.onegravity.bloc.utils.ReducerContext
import com.onegravity.bloc.utils.ReducerContextNoAction
import com.onegravity.bloc.utils.ThunkContext
import com.onegravity.bloc.utils.ThunkContextNoAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public fun <State, Action> ThunkContextNoAction<State, Action>.launchIt(
    cancelBeforeLaunch: Boolean = false,
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch { block() }
}

public fun <State, Action, A : Action> ThunkContext<State, Action, A>.launchIt(
    cancelBeforeLaunch: Boolean = false,
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch { block() }
}

public fun <State, Action> ReducerContext<State, Action>.launchIt(
    cancelBeforeLaunch: Boolean = false,
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch { block() }
}

public fun <State> ReducerContextNoAction<State>.launchIt(
    cancelBeforeLaunch: Boolean = false,
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch { block() }
}
