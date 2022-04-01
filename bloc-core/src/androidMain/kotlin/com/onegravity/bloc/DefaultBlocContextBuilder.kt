/**
 * From https://github.com/arkivanov/Decompose
 */

@file:Suppress("SpellCheckingInspection")

package com.onegravity.bloc

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.*
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.onegravity.bloc.context.DefaultBlocContext

fun ComponentActivity.activityBlocContext() = ActivityBlocContext(
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

/**
 * If you use this (from an Activity or a Fragment) the Bloc needs to make sure to retain its state
 * "manually" using the InstanceKeeper like:
 * ```
 *   private val blocState = context.instanceKeeper.getOrCreate("BLOC_STATE") {
 *      blocState(initialState)
 *   }
 * ```
 */
inline fun <T, reified C> T.defaultBlocContext(block: DefaultBlocContext.() -> C): C where
        T : SavedStateRegistryOwner, T : OnBackPressedDispatcherOwner, T : ViewModelStoreOwner, T : LifecycleOwner =
    createBlocContext().run {
        instanceKeeper.getOrCreate(C::class.java)  { InstanceKeeperWrapper(block()) }.component
    }

class InstanceKeeperWrapper<C>(val component: C) : InstanceKeeper.Instance {
    override fun onDestroy() {}
}

fun <T> T.createBlocContext(): DefaultBlocContext where
        T : SavedStateRegistryOwner, T : OnBackPressedDispatcherOwner, T : ViewModelStoreOwner, T : LifecycleOwner {
    val viewModel = viewModelStore.let(::blocViewModel)
    return DefaultBlocContext(
        lifecycle = viewModel.lifecycleRegistry,
        stateKeeper = savedStateRegistry.let(::StateKeeper),
        instanceKeeper = viewModel.instanceKeeperDispatcher,
        backPressedHandler = onBackPressedDispatcher.let(::BackPressedHandler)
    )
}

/**
 * Creates a new instance of [Lifecycle] and attaches it to the provided AndroidX [ViewModelStore]
 */
private fun blocViewModel(viewModelStore: ViewModelStore): BlocViewModel =
    ViewModelProvider(
        viewModelStore,
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) = BlocViewModel() as T
        }
    ).get()

internal class BlocViewModel : ViewModel() {
    val lifecycleRegistry = LifecycleRegistry()
    val instanceKeeperDispatcher = InstanceKeeperDispatcher()

    init {
        lifecycleRegistry.create()
    }

    override fun onCleared() {
        lifecycleRegistry.destroy()
        instanceKeeperDispatcher.destroy()
    }
}
