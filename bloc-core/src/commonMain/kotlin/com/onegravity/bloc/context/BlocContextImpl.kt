package com.onegravity.bloc.context

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy

public class BlocContextImpl(
    override val lifecycle: Lifecycle,
    instanceKeeper: InstanceKeeper? = null
) : BlocContext {

    /**
     * For iOS
     */
    public constructor(lifecycle: Lifecycle) : this(lifecycle, null)

    override val instanceKeeper: InstanceKeeper = instanceKeeper
        ?: InstanceKeeperDispatcher().apply { lifecycle.doOnDestroy(::destroy) }

}
