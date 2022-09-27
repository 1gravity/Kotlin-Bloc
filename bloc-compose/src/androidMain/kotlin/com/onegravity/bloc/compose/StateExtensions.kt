package com.onegravity.bloc.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
 * Adapter for Jetpack / Jetbrains Compose to observe a Bloc's state as
 * androidx.compose.runtime.State:
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
 * Adapter for Jetpack / Jetbrains Compose to observe a BlocOwner's/Bloc's state as
 * androidx.compose.runtime.State:
 *
 *   val state = observeState()  // assuming `this` is a BlocOwner
 */
@Composable
fun <STATE : Any, ACTION : Any, SIDE_EFFECT : Any, PROPOSAL : Any> BlocOwner<STATE, ACTION, SIDE_EFFECT, PROPOSAL>.observeState() =
    bloc.observeState()

/**
 * Adapter for Jetpack / Jetbrains Compose to observe a BlocObservable's/Bloc's state as
 * androidx.compose.runtime.State:
 *
 *   val state = observable.observeState()
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
 * Adapter for Jetpack / Jetbrains Compose to observe a BlocObservableOwner's/Bloc's state as
 * androidx.compose.runtime.State:
 *
 *   val state = observeState()     // assuming `this` is a BlocObservableOwner
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> BlocObservableOwner<STATE, SIDE_EFFECT>.observeState() =
    observable.observeState()
