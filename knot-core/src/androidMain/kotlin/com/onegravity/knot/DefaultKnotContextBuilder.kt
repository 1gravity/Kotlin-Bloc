/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.knot

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.onegravity.bloc.context.DefaultBlocContext

fun AppCompatActivity.activityKnotContext() = ActivityKnotContext(
    savedStateRegistry = savedStateRegistry,
    viewModelStore = viewModelStore,
    onBackPressedDispatcher = onBackPressedDispatcher
)

data class ActivityKnotContext(
    val savedStateRegistry: SavedStateRegistry? = null,
    val viewModelStore: ViewModelStore? = null,
    val onBackPressedDispatcher: OnBackPressedDispatcher? = null
)

/** --------------------------------------------------------------------------------------------- */

fun ViewModel.defaultKnotContext(
    activityKnotContext: ActivityKnotContext,
    lifecycleRegistry: LifecycleRegistry
) = DefaultBlocContext(
    lifecycle = lifecycleRegistry.asEssentyLifecycle(),
    stateKeeper = activityKnotContext.savedStateRegistry?.let(::StateKeeper),
    instanceKeeper = activityKnotContext.viewModelStore?.let(::InstanceKeeper),
    backPressedHandler = activityKnotContext.onBackPressedDispatcher?.let(::BackPressedHandler)
)

/** --------------------------------------------------------------------------------------------- */

fun <T> T.defaultKnotContext(): DefaultBlocContext where
        T : SavedStateRegistryOwner, T : OnBackPressedDispatcherOwner, T : ViewModelStoreOwner, T : LifecycleOwner =
    DefaultBlocContext(
        lifecycle = (this as LifecycleOwner).lifecycle.asEssentyLifecycle(),
        stateKeeper = savedStateRegistry.let(::StateKeeper),
        instanceKeeper = viewModelStore.let(::InstanceKeeper),
        backPressedHandler = onBackPressedDispatcher.let(::BackPressedHandler)
    )
