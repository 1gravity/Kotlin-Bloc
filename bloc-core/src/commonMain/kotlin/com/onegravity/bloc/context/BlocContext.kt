/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.bloc.context

import com.arkivanov.essenty.backpressed.BackPressedHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner

interface BlocContext : LifecycleOwner, InstanceKeeperOwner, BackPressedHandlerOwner
