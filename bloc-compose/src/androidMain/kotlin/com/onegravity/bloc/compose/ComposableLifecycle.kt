package com.onegravity.bloc.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.onegravity.bloc.BlocContext

/**
 * Creates a BlocContext for compose previews with a Composable lifecycle
 */
@Composable
fun previewBlocContext(): BlocContext = object : BlocContext {
    override val lifecycle = composableLifecycle()
}

/**
 * Create an Essenty lifecycle for Composable previews.
 * It's tied to the lifecycle of the Composable (DisposableEffect).
 */
@Composable
private fun composableLifecycle(): Lifecycle {
    val registry = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        registry.resume()
        onDispose { registry.destroy() }
    }

    return registry
}
