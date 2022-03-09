/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.knot.context

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.coroutineScope
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.Lifecycle as AndroidLifecycle

fun DefaultKnotContext(
    lifecycle: AndroidLifecycle,
    coroutineScope: CoroutineScope,
    savedStateRegistry: SavedStateRegistry? = null,
    viewModelStore: ViewModelStore? = null,
    onBackPressedDispatcher: OnBackPressedDispatcher? = null
): DefaultKnotContext =
    DefaultKnotContext(
        lifecycle = lifecycle.asEssentyLifecycle(),
        coroutineScope = lifecycle.coroutineScope,
        stateKeeper = savedStateRegistry?.let(::StateKeeper),
        instanceKeeper = viewModelStore?.let(::InstanceKeeper),
        backPressedHandler = onBackPressedDispatcher?.let(::BackPressedHandler)
    )

fun <T> T.defaultKnotContext(): DefaultKnotContext where
    T : SavedStateRegistryOwner,
    T : OnBackPressedDispatcherOwner,
    T : ViewModelStoreOwner,
    T : LifecycleOwner =
    DefaultKnotContext(
        lifecycle = (this as LifecycleOwner).lifecycle,
        coroutineScope = (this as LifecycleOwner).lifecycle.coroutineScope,
        savedStateRegistry = savedStateRegistry,
        viewModelStore = viewModelStore,
        onBackPressedDispatcher = onBackPressedDispatcher
    )
