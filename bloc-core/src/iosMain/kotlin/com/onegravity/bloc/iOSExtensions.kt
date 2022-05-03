package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*

/**
 * Subscribes to the state and side effects streams of a Bloc.
 *
 * The subscription is tied to the lifecycle of the caller. The subscription starts with onStart()
 * and is cancelled with onStop().
 */
@BlocDSL
fun <State, Action : Any, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.subscribeIOS(
    lifecycle: Lifecycle,
    state: ((state: State) -> Unit)? = null,
    sideEffect: ((sideEffect: SideEffect) -> Unit)? = null
) = bloc.subscribeIOS(lifecycle, state, sideEffect)

@BlocDSL
fun <State, Action : Any, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.subscribeIOS(
    lifecycle: Lifecycle,
    state: ((state: State) -> Unit)? = null,
    sideEffect: ((sideEffect: SideEffect) -> Unit)? = null
) {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    lifecycle.doOnStart {
        logger.d("onStart -> start subscription")
        state?.let {
            coroutineScope.launch {
                collect { state(it) }
            }
        }
        sideEffect?.let {
            coroutineScope.launch {
                sideEffects.collect { sideEffect(it) }
            }
        }
    }

    lifecycle.doOnStop {
        logger.d("onStop -> stop subscription")
        coroutineScope.cancel("Stop Subscription")
    }
}