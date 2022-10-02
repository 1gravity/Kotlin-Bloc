@file:Suppress("UNCHECKED_CAST")

package com.onegravity.bloc.utils

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.BlocObservableOwner
import com.onegravity.bloc.BlocOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Subscribes to the state and side effects streams of a Bloc.
 *
 * The subscription is tied to the lifecycle of the caller. The subscription starts with onStart()
 * and is cancelled with onStop().

 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any> Bloc<State, Action, SideEffect>.subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    lifecycle.doOnStart {
        logger.d("start Bloc subscription")

        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

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

        lifecycle.doOnStop {
            logger.d("stop Bloc subscription")
            coroutineScope.cancel("stop Bloc subscription")
        }
    }
}

/**
 * Call from a component to observe state and side effect updates in a BlocOwner
 * (BlocOwner in Android is typically a ViewModel, the observing component a Fragment or
 * an Activity):
 * ```
 *   component.subscribe(lifecycle, state = ::render, sideEffect = ::sideEffect)
 * ```
 *
 * Note: there are extension functions for Fragments and Activities to get an Essenty lifecycle,
 * so a call typically looks like:
 * ```
 *   component.subscribe(this, state = ::render, sideEffect = ::sideEffect)
 * ```
 */
@BlocDSL
public fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any>
        BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    bloc.subscribe(lifecycle, state, sideEffect)
}

/**
 * Analogous call for BlocObservableOwner
 */
@BlocDSL
public fun <State : Any, SideEffect : Any> BlocObservableOwner<State, SideEffect>.subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(lifecycle, state, sideEffect)
}
