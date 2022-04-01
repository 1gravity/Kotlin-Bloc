/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.bloc.context

import com.arkivanov.essenty.backpressed.BackPressedHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.statekeeper.StateKeeperOwner

// TODO investigate if we should replace LifecycleOwner by a CoroutineScope since we use the
//      lifecycle mostly for structured concurrency with coroutines?
interface BlocContext :
    LifecycleOwner,
    StateKeeperOwner,
    InstanceKeeperOwner,
    BackPressedHandlerOwner
