/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.knot

import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistry
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.onegravity.knot.context.DefaultKnotContext
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.Lifecycle as AndroidLifecycle

data class ActivityKnotContext(
    val savedStateRegistry: SavedStateRegistry? = null,
    val viewModelStore: ViewModelStore? = null,
    val onBackPressedDispatcher: OnBackPressedDispatcher? = null
)

fun AppCompatActivity.defaultKnotContext() = ActivityKnotContext(
    savedStateRegistry = savedStateRegistry,
    viewModelStore = viewModelStore,
    onBackPressedDispatcher = onBackPressedDispatcher
)

fun ViewModel.defaultKnotContext(
    activityKnotContext: ActivityKnotContext,
    lifecycleRegistry: LifecycleRegistry
) = defaultKnotContext(
    lifecycle = lifecycleRegistry,
    coroutineScope = viewModelScope,
    activityKnotContext = activityKnotContext
)

fun defaultKnotContext(
    lifecycle: AndroidLifecycle,
    coroutineScope: CoroutineScope,
    activityKnotContext: ActivityKnotContext
) = DefaultKnotContext(
    lifecycle = lifecycle.asEssentyLifecycle(),
    coroutineScope = coroutineScope,
    stateKeeper = activityKnotContext.savedStateRegistry?.let(::StateKeeper),
    instanceKeeper = activityKnotContext.viewModelStore?.let(::InstanceKeeper),
    backPressedHandler = activityKnotContext.onBackPressedDispatcher?.let(::BackPressedHandler)
)
