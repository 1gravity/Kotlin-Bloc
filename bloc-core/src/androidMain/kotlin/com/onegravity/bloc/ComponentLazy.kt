package com.onegravity.bloc

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.onegravity.bloc.context.BlocContext
import kotlin.reflect.KClass

class ComponentLazy<A, Component : Any>(
    private val owner: Lazy<A>,
    private val clazz: KClass<Component>,
    private val createInstance: (context: BlocContext) -> Component
) : Lazy<Component>
        where A : OnBackPressedDispatcherOwner,
              A : ViewModelStoreOwner,
              A : LifecycleOwner {

    private var cached: Component? = null

    override val value: Component
        get() = cached
            ?: owner.value.createBlocContext().run {
                instanceKeeper.getOrCreate(clazz.java) {
                    InstanceWrapper(createInstance(this))
                }.component
            }.also { cached = it }

    override fun isInitialized(): Boolean = cached != null

}
