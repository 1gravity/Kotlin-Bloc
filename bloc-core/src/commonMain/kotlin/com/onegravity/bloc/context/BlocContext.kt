package com.onegravity.bloc.context

import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner

public interface BlocContext : LifecycleOwner, InstanceKeeperOwner
