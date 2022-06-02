package com.onegravity.bloc

import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
 * Subscribes to a BlocObservableOwner (typically a ViewModel, a Fragment or an Activity).
 * The subscription is tied to the lifecycle of a LifecycleOwner (normally an Activity or a Fragment).
 */
@BlocDSL
fun <State : Any, SideEffect : Any> BlocObservableOwner<State, SideEffect>.subscribe(
    lifecycleOwner: LifecycleOwner,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(lifecycleOwner.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

/**
 * Same as above for a BlocOwner
 */
@BlocDSL
fun <State : Any, Action : Any, SideEffect : Any, Proposal : Any> BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
    lifecycleOwner: LifecycleOwner,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    bloc.subscribe(lifecycleOwner.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

/**
 * Same as above for a Bloc
 */
@BlocDSL
fun <State : Any, Action : Any, SideEffect : Any> Bloc<State, Action, SideEffect>.subscribe(
    lifecycleOwner: LifecycleOwner,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    subscribe(lifecycleOwner.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

/**
 * Adapter(s) for Android to use Bloc as LiveData to be used with Data Binding.
 * ```
 *   val state = bloc.toLiveData(viewModelScope)
 * ```
 */
@BlocDSL
fun <State : Any, Action : Any, SideEffect : Any> Bloc<State, Action, SideEffect>.toLiveData(scope: CoroutineScope): LiveData<State> =
    MutableLiveData<State>().apply {
        scope.launch {
            collect { value = it }
        }
    }

/**
 * ViewModel
 */

/**
 * Expose a Bloc as LiveData<State> so it can be used with Android data binding, e.g.:
 * ```
 *   val state = toLiveData(bloc)
 * ```
 */
@BlocDSL
fun <State : Any, Action : Any, SideEffect : Any> ViewModel.toLiveData(bloc: Bloc<State, Action, SideEffect>) =
    bloc.toLiveData(viewModelScope)

/**
 * The same for Activities / Fragments
 */
@BlocDSL
fun <State : Any, Action : Any, SideEffect : Any> LifecycleOwner.toLiveData(bloc: Bloc<State, Action, SideEffect>) =
    bloc.toLiveData(lifecycleScope)
