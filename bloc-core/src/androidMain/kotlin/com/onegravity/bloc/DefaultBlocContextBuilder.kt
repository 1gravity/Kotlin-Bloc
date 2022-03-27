/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.bloc

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.onegravity.bloc.context.DefaultBlocContext

fun AppCompatActivity.activityBlocContext() = ActivityBlocContext(
    savedStateRegistry = savedStateRegistry,
    viewModelStore = viewModelStore,
    onBackPressedDispatcher = onBackPressedDispatcher
)

fun Fragment.activityBlocContext() = ActivityBlocContext(
    savedStateRegistry = savedStateRegistry,
    viewModelStore = viewModelStore,
    onBackPressedDispatcher = activity?.onBackPressedDispatcher
)

data class ActivityBlocContext(
    val savedStateRegistry: SavedStateRegistry? = null,
    val viewModelStore: ViewModelStore? = null,
    val onBackPressedDispatcher: OnBackPressedDispatcher? = null
)

/** --------------------------------------------------------------------------------------------- */

fun ActivityBlocContext.toBlocContext(
    lifecycleRegistry: LifecycleRegistry
) = DefaultBlocContext(
    lifecycle = lifecycleRegistry.asEssentyLifecycle(),
    stateKeeper = savedStateRegistry?.let(::StateKeeper),
    instanceKeeper = viewModelStore?.let(::InstanceKeeper),
    backPressedHandler = onBackPressedDispatcher?.let(::BackPressedHandler)
)

fun defaultBlocContext(
    activityBlocContext: ActivityBlocContext,
    lifecycleRegistry: LifecycleRegistry
) = DefaultBlocContext(
    lifecycle = lifecycleRegistry.asEssentyLifecycle(),
    stateKeeper = activityBlocContext.savedStateRegistry?.let(::StateKeeper),
    instanceKeeper = activityBlocContext.viewModelStore?.let(::InstanceKeeper),
    backPressedHandler = activityBlocContext.onBackPressedDispatcher?.let(::BackPressedHandler)
)

/** --------------------------------------------------------------------------------------------- */

fun <T> T.defaultBlocContext(): DefaultBlocContext where
        T : SavedStateRegistryOwner, T : OnBackPressedDispatcherOwner, T : ViewModelStoreOwner, T : LifecycleOwner =
    DefaultBlocContext(
        lifecycle = (this as LifecycleOwner).lifecycle.asEssentyLifecycle(),
        stateKeeper = savedStateRegistry.let(::StateKeeper),
        instanceKeeper = viewModelStore.let(::InstanceKeeper),
        backPressedHandler = onBackPressedDispatcher.let(::BackPressedHandler)
    )
