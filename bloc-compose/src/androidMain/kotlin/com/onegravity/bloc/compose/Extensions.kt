package com.onegravity.bloc.compose

import androidx.compose.runtime.*
import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.*
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*

/**
 * Adapter for Jetpack / Jetbrains Compose to observe bloc state as androidx.compose.runtime.State:
 *
 *   val state = bloc.observeState()
 */
@Composable
fun <STATE : Any, ACTION : Any, SIDE_EFFECT : Any> Bloc<STATE, ACTION, SIDE_EFFECT>.observeState(): State<STATE> {
    val state = remember(this) { mutableStateOf(value) }

    DisposableEffect(this) {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        coroutineScope.launch {
            this@observeState.collect {
                state.value = it
            }
        }

        onDispose {
            coroutineScope.cancel()
        }
    }

    return state
}

/**
 * The same for BlocOwner
 */
@Composable
fun <STATE : Any, ACTION : Any, SIDE_EFFECT : Any, PROPOSAL : Any> BlocOwner<STATE, ACTION, SIDE_EFFECT, PROPOSAL>.observeState() =
    bloc.observeState()

/**
 * The same for BlocObservable
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> BlocObservable<STATE, SIDE_EFFECT>.observeState(): State<STATE> {
    val state = remember(this) { mutableStateOf(value) }

    val lifecycleRegistry = LifecycleRegistry()

    DisposableEffect(this) {
        lifecycleRegistry.create()
        lifecycleRegistry.start()

        this@observeState.subscribe(lifecycle = lifecycleRegistry, state = {
            state.value = it
        })

        onDispose {
            lifecycleRegistry.stop()
            lifecycleRegistry.destroy()
        }
    }

    return state
}

/**
 * The same for BlocObservableOwner
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> BlocObservableOwner<STATE, SIDE_EFFECT>.observeState() =
    observable.observeState()

/**
 * Adapter for Jetpack / Jetbrains Compose to observe bloc side effects as
 * androidx.compose.runtime.State:
 *
 *   val state = bloc.observeState()
 */
@Composable
fun <STATE : Any, ACTION : Any, SIDE_EFFECT : Any> Bloc<STATE, ACTION, SIDE_EFFECT>.observeSideEffects(): State<SIDE_EFFECT?> {
    val state: MutableState<SIDE_EFFECT?> = remember(this) { mutableStateOf(null) }

    DisposableEffect(this) {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        coroutineScope.launch {
            this@observeSideEffects.sideEffects.collect {
                // we need to force the ui to recompose even if we emit the same value twice
                // and since side effects can have duplicates we use null to "reset" the State
                // before emitting the new/old value
                // SideEffectStreams have no initial value and using null values is the recommended
                // way to use State / StateFlow if there's no initial value
                // (see: https://github.com/Kotlin/kotlinx.coroutines/issues/2515)
                state.value = null
                state.value = it
            }
        }

        onDispose {
            coroutineScope.cancel()
        }
    }

    return state
}

/**
 * The same for BlocOwner
 */
@Composable
fun <STATE : Any, ACTION : Any, SIDE_EFFECT : Any, PROPOSAL : Any> BlocOwner<STATE, ACTION, SIDE_EFFECT, PROPOSAL>.observeSideEffects() =
    bloc.observeSideEffects()

/**
 * The same for BlocObservable
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> BlocObservable<STATE, SIDE_EFFECT>.observeSideEffects(): State<SIDE_EFFECT?> {
    val state: MutableState<SIDE_EFFECT?> = remember(this) { mutableStateOf(null) }

    val lifecycleRegistry = LifecycleRegistry()

    DisposableEffect(this) {
        lifecycleRegistry.create()
        lifecycleRegistry.start()

        this@observeSideEffects.subscribe(lifecycle = lifecycleRegistry, sideEffect = {
            // we need to force the ui to recompose even if we emit the same value twice
            // and since side effects can have duplicates we use null to "reset" the State
            // before emitting the new/old value
            // SideEffectStreams have no initial value and using null values is the recommended
            // way to use State / StateFlow if there's no initial value
            // (see: https://github.com/Kotlin/kotlinx.coroutines/issues/2515)
            state.value = null
            state.value = it
        })

        onDispose {
            lifecycleRegistry.stop()
            lifecycleRegistry.destroy()
        }
    }

    return state
}

/**
 * The same for BlocObservableOwner
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> BlocObservableOwner<STATE, SIDE_EFFECT>.observeSideEffects() =
    observable.observeSideEffects()
