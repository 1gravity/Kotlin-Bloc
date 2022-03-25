package com.onegravity.bloc

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.onegravity.bloc.utils.BlocObservableOwner
import com.onegravity.bloc.utils.StateStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * AppCompatActivity.
 */

fun <T : ViewDataBinding> AppCompatActivity.bind(@LayoutRes layoutId: Int, bind2ViewModel: (T) -> Unit) {
    val binding = DataBindingUtil.setContentView<T>(this, layoutId)
    bind2ViewModel(binding)
    binding.lifecycleOwner = this
    setContentView(binding.root)
}

@Suppress("UNCHECKED_CAST")
inline fun <VM : ViewModel> AppCompatActivity.factory(crossinline f: (context: ActivityBlocContext) -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>):T = f(activityBlocContext()) as T
    }

/**
 * Subscribes to a BlocObservableOwner (typically a ViewModel). The subscription
 */
fun <State, SideEffect> BlocObservableOwner<State, SideEffect>.subscribe(
    activity: AppCompatActivity,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(activity.lifecycle.asEssentyLifecycle(), state, sideEffect)
}

/**
 * Fragment.
 */

@Suppress("UNCHECKED_CAST")
inline fun <VM : ViewModel> Fragment.factory(crossinline f: (context: ActivityBlocContext) -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>):T = f(activityBlocContext()) as T
    }

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
fun <State> StateStream<State>.toLiveData(scope: CoroutineScope): LiveData<State> =
    MutableLiveData<State>().apply {
        scope.launch {
            collect { value = it }
        }
    }

/**
 * Call from a ViewModel:
 * ```
 *   val state = toLiveData(bloc)
 * ```
 */
fun <State> ViewModel.toLiveData(stream: StateStream<State>) = stream.toLiveData(viewModelScope)
