/**
 * From https://github.com/arkivanov/Decompose
 */

package com.onegravity.knot.context

import com.arkivanov.essenty.backpressed.BackPressedDispatcher
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import kotlinx.coroutines.CoroutineScope

class DefaultKnotContext(
    override val lifecycle: Lifecycle,
    override val coroutineScope: CoroutineScope,
    stateKeeper: StateKeeper? = null,
    instanceKeeper: InstanceKeeper? = null,
    backPressedHandler: BackPressedHandler? = null,
) : KnotContext {

    override val stateKeeper: StateKeeper = stateKeeper ?: StateKeeperDispatcher()
    override val instanceKeeper: InstanceKeeper = instanceKeeper ?: InstanceKeeperDispatcher().attachTo(lifecycle)
    override val backPressedHandler: BackPressedHandler = backPressedHandler ?: BackPressedDispatcher()

}
