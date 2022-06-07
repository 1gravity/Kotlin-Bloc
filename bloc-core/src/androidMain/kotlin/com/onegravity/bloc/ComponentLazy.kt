package com.onegravity.bloc

import androidx.lifecycle.*
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.context.BlocContextImpl

class ComponentLazy<A: ViewModelStoreOwner, Component : Any>(
    private val owner: Lazy<A>,
    private val key: Any,
    private val create: (context: BlocContext) -> Component
) : Lazy<Component> {

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
private class InstanceWrapper<C>(val component: C) : InstanceKeeper.Instance {
    override fun onDestroy() { }
}

/**
 * This creates the actual BlocContext.
 *
 * It creates a BlocViewModel and stores it in Android's ViewModelStore.
 */
private fun <T: ViewModelStoreOwner> T.createBlocContext(): BlocContext {
    val viewModel = viewModelStore.blocViewModel()
    return BlocContextImpl(
        lifecycle = viewModel.lifecycleRegistry
    )
}

/**
 * Get or create the BlocViewModel.
 */
@Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
private fun ViewModelStore.blocViewModel(): BlocViewModel =
    ViewModelProvider(
        this,
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) = BlocViewModel() as T
        }
    ).get()
