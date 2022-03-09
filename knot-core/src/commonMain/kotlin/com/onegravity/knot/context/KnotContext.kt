/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.knot.context

import com.arkivanov.essenty.backpressed.BackPressedHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.statekeeper.StateKeeperOwner

interface KnotContext :
    LifecycleOwner,
    CoroutineScopeOwner,
    StateKeeperOwner,
    InstanceKeeperOwner,
    BackPressedHandlerOwner