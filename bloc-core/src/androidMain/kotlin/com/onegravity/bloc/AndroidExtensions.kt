package com.onegravity.bloc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onegravity.bloc.utils.Stream
import com.onegravity.bloc.utils.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
