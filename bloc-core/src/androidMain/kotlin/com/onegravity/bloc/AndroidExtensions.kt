package com.onegravity.bloc

import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*

/**
 * ComponentActivity / AppCompatActivity / Fragment
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
 * Subscribes to a BlocObservableOwner (typically a ViewModel).
 * The subscription is tied to the lifecycle of a LifecycleOwner (typically an Activity or a Fragment).
 */
@BlocDSL
fun <State: Any, SideEffect: Any> BlocObservableOwner<State, SideEffect>.subscribe(
    lifecycleOwner: LifecycleOwner,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(lifecycleOwner.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

@BlocDSL
fun <State: Any, Action: Any, SideEffect: Any, Proposal: Any> BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
    lifecycleOwner: LifecycleOwner,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    bloc.toObservable().subscribe(lifecycleOwner.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

/**
 * Adapter(s) for Android to use [Stream]s as LiveData to be used with Data Binding.
 * ```
 *   val state = bloc.toLiveData(viewModelScope)
 * ```
 */
@BlocDSL
fun <State: Any> StateStream<State>.toLiveData(scope: CoroutineScope): LiveData<State> =
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
fun <State: Any> ViewModel.toLiveData(stream: StateStream<State>) =
    stream.toLiveData(viewModelScope)

/**
 * The same for Activities / Fragments
 */
@BlocDSL
fun <State: Any> LifecycleOwner.toLiveData(stream: StateStream<State>) =
    stream.toLiveData(lifecycleScope)

