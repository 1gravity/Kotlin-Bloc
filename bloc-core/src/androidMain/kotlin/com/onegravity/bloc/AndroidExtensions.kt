package com.onegravity.bloc

import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.compose.runtime.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*

/**
 * ComponentActivity / AppCompatActivity
 */

@BlocDSL
fun <T : ViewDataBinding> ComponentActivity.bind(
    @LayoutRes layoutId: Int,
    bind2ViewModel: (T) -> Unit
) {
    val binding = DataBindingUtil.setContentView<T>(this, layoutId)
    bind2ViewModel(binding)
    binding.lifecycleOwner = this
    setContentView(binding.root)
}

/**
 * Subscribes to a BlocObservableOwner (typically a ViewModel). The subscription
 */
@BlocDSL
fun <State, SideEffect> BlocObservableOwner<State, SideEffect>.subscribe(
    activity: ComponentActivity,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(activity.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

@BlocDSL
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
    activity: ComponentActivity,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    bloc.toObservable().subscribe(activity.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

/**
 * Fragment
 */

@BlocDSL
fun <State, Action, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
    fragment: Fragment,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    bloc.toObservable().subscribe(fragment.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

@BlocDSL
fun <State, SideEffect> BlocObservableOwner<State, SideEffect>.subscribe(
    fragment: Fragment,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(fragment.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

/**
 * Adapter(s) for Android to use [Stream]s as LiveData to be used with Data Binding.
 * ```
 *   val state = bloc.toLiveData(viewModelScope)
 * ```
 */
@BlocDSL
fun <State> StateStream<State>.toLiveData(scope: CoroutineScope): LiveData<State> =
    MutableLiveData<State>().apply {
        scope.launch {
            collect { value = it }
        }
    }

/**
 * ViewModel
 */

/**
 * Expose a StateStream<State> as LiveData<State> so it can be used with Android data binding, e.g.:
 * ```
 *   val state = toLiveData(bloc)
 * ```
 */
@BlocDSL
fun <State> ViewModel.toLiveData(stream: StateStream<State>) = stream.toLiveData(viewModelScope)

/**
 * The same for Activities
 */
@BlocDSL
fun <State> LifecycleOwner.toLiveData(stream: StateStream<State>) = stream.toLiveData(lifecycleScope)

/**
 * Adapter for Jetbrains Compose.
 *
 * This allows us to select sub state and subscribe to it as androidx.compose.runtime.State in one
 * line of code:
 *   val state = bloc.subscribeAsState()
 */
@Composable
fun <S, Action, SideEffect> BlocFacade<S, Action, SideEffect>.observeState(): State<S> {
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
fun <S, Action, SideEffect, Proposal> BlocOwner<S, Action, SideEffect, Proposal>.observeState() =
    bloc.observeState()

@Composable
fun <S, Action, SideEffect> BlocFacade<S, Action, SideEffect>.observeSideEffects(): State<SideEffect?> {
    val state: MutableState<SideEffect?> = remember(this) { mutableStateOf(null) }
    DisposableEffect(this) {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        coroutineScope.launch {
            this@observeSideEffects.sideEffects.collect {
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
fun <S, Action, SideEffect, Proposal> BlocOwner<S, Action, SideEffect, Proposal>.observeSideEffects() =
    bloc.observeSideEffects()
