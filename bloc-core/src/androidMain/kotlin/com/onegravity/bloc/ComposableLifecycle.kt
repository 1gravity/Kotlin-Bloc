package com.onegravity.bloc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume

@Composable
internal fun composableLifecycle(): Lifecycle {
    val registry = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        registry.resume()
        onDispose { registry.destroy() }
    }

    return registry
}