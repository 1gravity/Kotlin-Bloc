package com.onegravity.bloc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onegravity.bloc.utils.Stream
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
