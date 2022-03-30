/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.bloc.context

import com.arkivanov.essenty.backpressed.BackPressedHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.statekeeper.StateKeeperOwner

// TODO should we replace LifecycleOwner by a CoroutineScope since we use the lifecycle mostly for
//      structured concurrency with coroutines?
// TODO implement one example using StateKeeperOwner instead of a ViewModel
// TODO implement one example using InstanceKeeperOwner to demo state persistence even after the app "terminated"
interface BlocContext :
    LifecycleOwner,
    StateKeeperOwner,
    InstanceKeeperOwner,
    BackPressedHandlerOwner
