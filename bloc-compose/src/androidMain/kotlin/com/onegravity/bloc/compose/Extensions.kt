package com.onegravity.bloc.compose

import androidx.compose.runtime.Composable
import kotlinx.coroutines.*
import androidx.compose.runtime.*
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.context.DefaultBlocContext
import com.onegravity.bloc.utils.BlocOwner

/**
 * Adapter for Jetbrains Compose.
 *
 * This allows us to select sub state and subscribe to it as androidx.compose.runtime.State in one
 * line of code:
 *   val state = bloc.observeState()
 */
@Composable
fun <S: Any, Action: Any, SideEffect: Any> Bloc<S, Action, SideEffect>.observeState(): State<S> {
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

@Composable
fun <S: Any, Action: Any, SideEffect: Any, Proposal: Any> BlocOwner<S, Action, SideEffect, Proposal>.observeState() =
    bloc.observeState()

@Composable
fun <S: Any, Action: Any, SideEffect: Any> Bloc<S, Action, SideEffect>.observeSideEffects(): State<SideEffect?> {
    val state: MutableState<SideEffect?> = remember(this) { mutableStateOf(null) }
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
 * Creates DefaultBlocContext for compose previews with a Composable life cycle but without the
 * other parameters (StateKeeper, InstanceKeeper, BackPressedHandler)
 */
@Composable
fun previewBlocContext(): BlocContext = DefaultBlocContext(composableLifecycle())

@Composable
fun <S: Any, Action: Any, SideEffect: Any, Proposal: Any> BlocOwner<S, Action, SideEffect, Proposal>.observeSideEffects() =
    bloc.observeSideEffects()
