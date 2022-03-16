package com.onegravity.bloc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Adapter for Android to use [Stream]s as LiveData to be used with Data Binding.
 */
fun <State> Stream<State>.toLiveData(scope: CoroutineScope): LiveData<State> =
    MutableLiveData<State>().apply {
        scope.launch {
            collect { value = it }
        }
    }
