package com.onegravity.bloc.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume

/**
 * Right now we're using this to create a lifecycle for Composable previews
 */
@Composable
internal fun composableLifecycle(): Lifecycle {
    val registry = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        registry.resume()
        onDispose { registry.destroy() }
    }

    return registry
}