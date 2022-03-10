package com.onegravity.knot

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <State> Stream<State>.toLiveData(scope: CoroutineScope) = MutableLiveData<State>().apply {
    scope.launch {
        collect { value = it }
    }
}
