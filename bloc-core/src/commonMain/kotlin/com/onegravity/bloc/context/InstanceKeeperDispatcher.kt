/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.bloc.context

import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy

internal fun InstanceKeeperDispatcher.attachTo(lifecycle: Lifecycle): InstanceKeeperDispatcher {
    lifecycle.doOnDestroy(::destroy)

    return this
}
