package com.onegravity.bloc

import androidx.annotation.LayoutRes
import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*

/**
 * ComponentActivity/AppCompatActivity.
 */

@BlocDSL
fun <T : ViewDataBinding> ComponentActivity.bind(@LayoutRes layoutId: Int, bind2ViewModel: (T) -> Unit) {
    val binding = DataBindingUtil.setContentView<T>(this, layoutId)
    bind2ViewModel(binding)
    binding.lifecycleOwner = this
    setContentView(binding.root)
}

@Suppress("UNCHECKED_CAST")
@BlocDSL
inline fun <VM : ViewModel> ComponentActivity.factory(crossinline createInstance: (context: ActivityBlocContext) -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>):T = createInstance(activityBlocContext()) as T
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
fun <State, Action : Any, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
    activity: ComponentActivity,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    bloc.toObservable().subscribe(activity.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

/**
 * Fragment.
 */

@Suppress("UNCHECKED_CAST")
@BlocDSL
inline fun <VM : ViewModel> Fragment.factory(crossinline f: (context: ActivityBlocContext) -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>):T = f(activityBlocContext()) as T
    }

@BlocDSL
fun <State, Action : Any, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
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
 * ViewModel.
 */

/**
 * Call from a ViewModel:
 * ```
 *   val state = toLiveData(bloc)
 * ```
 */
@BlocDSL
fun <State> ViewModel.toLiveData(stream: StateStream<State>) = stream.toLiveData(viewModelScope)

fun ViewModel.blocContext(context: ActivityBlocContext): BlocContext =
    BlocContextOwnerImpl(this, context).blocContext

fun ViewModel.blocContext(): BlocContext =
    BlocContextOwnerImpl(this, ActivityBlocContext(null, null, null)).blocContext

/**
 * Adapter for Jetbrains Compose.
 *
 * This allows us to select sub state and subscribe to it as androidx.compose.runtime.State in one
 * line of code:
 *   val state = bloc.subscribeAsState()
 */
@Composable
fun <S, Action: Any, SideEffect> BlocFacade<S, Action, SideEffect>.observeState(): State<S> {
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
fun <S, Action: Any, SideEffect, Proposal> BlocOwner<S, Action, SideEffect, Proposal>
        .observeState() = bloc.observeState()

@Composable
fun <S, Action: Any, SideEffect> BlocFacade<S, Action, SideEffect>.observeSideEffects(): State<SideEffect?> {
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
fun <S, Action: Any, SideEffect, Proposal> BlocOwner<S, Action, SideEffect, Proposal>
        .observeSideEffects() = bloc.observeSideEffects()
