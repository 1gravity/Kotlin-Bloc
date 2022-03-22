package com.onegravity.bloc

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.onegravity.bloc.utils.Stream
import com.onegravity.bloc.utils.subscribe
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
 * Fragment.
 */

@Suppress("UNCHECKED_CAST")
inline fun <VM : ViewModel> Fragment.factory(crossinline f: (context: ActivityBlocContext) -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>):T = f(activityBlocContext()) as T
    }

/**
 * Adapter(s) for Android to use [Stream]s as LiveData to be used with Data Binding.
 * ```
 *   val state = bloc.toLiveData(viewModelScope)
 * ```
 */
fun <State> Stream<State>.toLiveData(scope: CoroutineScope): LiveData<State> =
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
fun <State> ViewModel.toLiveData(stream: Stream<State>) = stream.toLiveData(viewModelScope)

/**
 * Call from a Fragment/Activity:
 * ```
 *   viewModel.subscribe(state = ::render, sideEffect = ::sideEffect)
 * ```
 */
fun <State, Action: Any, SideEffect, Proposal> BlocOwner<State, Action, SideEffect, Proposal>.subscribe(
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    bloc.subscribe(state, sideEffect)
}
