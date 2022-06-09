package com.onegravity.bloc.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.internal.BlocContextImpl

/**
 * Creates a BlocContext for compose previews with a Composable lifecycle
 */
@Composable
fun previewBlocContext(): BlocContext = BlocContextImpl(composableLifecycle())

/**
 * Right now we're using this to create a lifecycle for Composable previews
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
