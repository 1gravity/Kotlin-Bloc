package com.onegravity.bloc.internal.lifecycle

internal interface Callbacks {
    fun onCreate() {}

    fun onInitialize() {}

    fun onStart() {}

    fun onStop() {}

    fun onDestroy() {}
}
