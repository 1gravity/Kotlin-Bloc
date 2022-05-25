/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.bloc.context

import com.arkivanov.essenty.backpressed.BackPressedDispatcher
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.lifecycle.Lifecycle

@Suppress("unused")
public class BlocContextImpl(
    override val lifecycle: Lifecycle,
    instanceKeeper: InstanceKeeper? = null,
    backPressedHandler: BackPressedHandler? = null,
) : BlocContext {

    /**
     * For iOS
     */
    public constructor(lifecycle: Lifecycle) : this(lifecycle, null, null)

    override val instanceKeeper: InstanceKeeper = instanceKeeper ?: InstanceKeeperDispatcher().attachTo(lifecycle)
    override val backPressedHandler: BackPressedHandler = backPressedHandler ?: BackPressedDispatcher()

}