package com.onegravity.bloc

import androidx.lifecycle.*
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.onegravity.bloc.internal.BlocContextImpl

class ComponentLazy<A: ViewModelStoreOwner, Component : Any>(
    private val owner: Lazy<A>,
    private val key: Any,
    private val create: (context: BlocContext) -> Component
) : Lazy<Component> {

    // we don't really need to cache the component since it's stored in the InstanceKeeper anyway
    // but we can use the cached value in isInitialized()
    private var cached: Component? = null

    override val value: Component
        get() = cached ?: createComponent()

    override fun isInitialized(): Boolean = cached != null

    /**
     * - the ViewModelStore stores the BlocViewModel
     * - the BlocViewModel creates an Essenty lifecycle tied to the view model's lifecycle
     * - the BlocViewModel also creates an Essenty InstanceKeeper
     * - the Component is created using the BlocViewModel's Essenty lifecycle
     * - the BlocViewModel's InstanceKeeper stores the Component
     * ViewModelStore -> BlocViewModel -> InstanceKeeper -> Component(BlocViewModel.Lifecycle)
     */
    private fun createComponent(): Component {
        val viewModel = blocViewModel(owner.value)
        val component = viewModel.instanceKeeper.getOrCreate(key) {
            val context = BlocContextImpl(lifecycle = viewModel.lifecycleRegistry)
            val component = create(context)
            InstanceWrapper(component)
        }.component
        return component.also { cached = it }
    }

    // get or create the BlocViewModel and store it in the ViewModelStore
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    private fun blocViewModel(storeOwner: ViewModelStoreOwner): BlocViewModel =
        ViewModelProvider(
            storeOwner,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>) = BlocViewModel() as T
            }
        ).get()

    /**
     * We wrap a component into an InstanceWrapper so that components don't have to implement the
     * InstanceKeeper.Instance interface.
     */
    private class InstanceWrapper<C>(val component: C) : InstanceKeeper.Instance {
        override fun onDestroy() { }
    }

}
