package com.onegravity.bloc

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.onegravity.bloc.context.BlocContext

class ComponentLazy<A, Component : Any>(
    private val owner: Lazy<A>,
    private val key: Any,
    private val create: (context: BlocContext) -> Component
) : Lazy<Component>
        where A : OnBackPressedDispatcherOwner,
              A : ViewModelStoreOwner,
              A : LifecycleOwner {

    private var cached: Component? = null

    override val value: Component
        get() = cached ?: owner.value.createBlocContext()
            .run { getInstance() }
            .also { cached = it }

    override fun isInitialized(): Boolean = cached != null

    private fun BlocContext.getInstance() = instanceKeeper.getOrCreate(key) {
        InstanceWrapper(create(this))
    }.component

}

/**
 * We wrap a component into an InstanceWrapper so that components don't have to implement the
 * InstanceKeeper.Instance interface.
 */
class InstanceWrapper<C>(val component: C) : InstanceKeeper.Instance {
    override fun onDestroy() {}
}
