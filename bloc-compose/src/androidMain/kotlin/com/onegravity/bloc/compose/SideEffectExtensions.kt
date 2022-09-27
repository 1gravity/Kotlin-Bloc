package com.onegravity.bloc.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.start
import com.arkivanov.essenty.lifecycle.stop
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.BlocObservable
import com.onegravity.bloc.BlocObservableOwner
import com.onegravity.bloc.BlocOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Adapter for Jetpack / Jetbrains Compose to observe a Bloc's side effects as
 * androidx.compose.runtime.State:
 *
 *   val sideEffects = bloc.observeSideEffects()
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
 * Adapter for Jetpack / Jetbrains Compose to observe a BlocOwner's/Bloc's side effects as
 * androidx.compose.runtime.State:
 *
 *   val sideEffects = observeSideEffects()  // assuming `this` is a BlocOwner
 */
@Composable
fun <STATE : Any, ACTION : Any, SIDE_EFFECT : Any, PROPOSAL : Any> BlocOwner<STATE, ACTION, SIDE_EFFECT, PROPOSAL>.observeSideEffects() =
    bloc.observeSideEffects()

/**
 * Adapter for Jetpack / Jetbrains Compose to observe a BlocObservable's/Bloc's side effects as
 * androidx.compose.runtime.State:
 *
 *   val sideEffects = observable.observeSideEffects()
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
 * Adapter for Jetpack / Jetbrains Compose to observe a BlocObservableOwner's/Bloc's side effects as
 * androidx.compose.runtime.State:
 *
 *   val sideEffects = observable.observeSideEffects()  // assuming `this` is a BlocObservableOwner
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> BlocObservableOwner<STATE, SIDE_EFFECT>.observeSideEffects() =
    observable.observeSideEffects()
