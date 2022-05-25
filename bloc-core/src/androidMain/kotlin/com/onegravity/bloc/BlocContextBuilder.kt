/**
 * From https://github.com/arkivanov/Decompose
 */

@file:Suppress("SpellCheckingInspection")

package com.onegravity.bloc

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.context.BlocContextImpl
import com.onegravity.bloc.utils.BlocDSL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Use this from an Activity to get or create a "Component" without directly involving a ViewModel,
 * e.g.:
 * ```
 *   val component by getOrCreate { MyComponent(it) }      // it is the BlocContext
 * ```
 * or:
 * ```
 *   val bloc by getOrCreate { bloc<Int, Int>(it, 2) { ... } }
 * ```
 * Any class that needs a BlocContext to be instantiated is considered a "Component" in the context
 * of this function.
 *
 * The component will be tied to a ViewModel which is created transparently.
 *
 * @param key As default Component::class is used as key to store and retrieve the component from
 *            the InstanceKeeper (which is tied to the ViewModel). Because all Bloc instances have
 *            the same class (its generic types have been erased), all Blocs would be stored with
 *            the same key. If we're using multiple Blocs tied to the same ViewModel (e.g. multiple
 *            fragments using different Blocs), we need to provide a key that identifies the Bloc.
 * E.g.:
 * ```
 *   val listBloc by getOrCreate("blocList") { listBloc(it) }
 *   val detailBloc by getOrCreate("detailBloc") { detailBloc(it) }
 * ```
 * Alternatively a bloc can be wrapped into a concrete class in which case Component::class would
 * work again as key.
 */
inline fun <A, reified Component : Any> A.getOrCreate(
    key: Any = Component::class,
    noinline create: (context: BlocContext) -> Component
): Lazy<Component> where
        A : OnBackPressedDispatcherOwner,
        A : ViewModelStoreOwner,
        A : LifecycleOwner =
    ComponentLazy(
        owner = ActivityLazy { this },
        key = key,
        create = create
    )

/**
 * The same from a fragment
 */
inline fun <reified Component : Any> Fragment.getOrCreate(
    key: Any = Component::class,
    noinline create: (context: BlocContext) -> Component
): Lazy<Component> = ComponentLazy(
    owner = ActivityLazy { requireActivity() },
    key = key,
    create = create
)

/**
 * This creates the actual BlocContext.
 *
 * It creates a ViewModel and stores it in Android's ViewModelStore. The ViewModel is then used to
 * create the Lifecycle and the InstanceKeeper while the SavedStateRegistry and the
 * OnBackPressedDispatcher are "taken" from the Activity.
 */
fun <T> T.createBlocContext(): BlocContext where
        T : OnBackPressedDispatcherOwner,
        T : ViewModelStoreOwner,
        T : LifecycleOwner {
    val viewModel = viewModelStore.blocViewModel()
    return BlocContextImpl(
        lifecycle = viewModel.lifecycleRegistry,
        instanceKeeper = viewModel.instanceKeeperDispatcher,
        backPressedHandler = onBackPressedDispatcher.let(::BackPressedHandler)
    )
}

/**
 * Get or create the ViewModel.
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

internal class BlocViewModel : ViewModel() {
    val lifecycleRegistry = LifecycleRegistry()
    val instanceKeeperDispatcher = InstanceKeeperDispatcher()

    init {
        lifecycleRegistry.create()
        lifecycleRegistry.start()
    }

    override fun onCleared() {
        lifecycleRegistry.stop()
        lifecycleRegistry.destroy()
        instanceKeeperDispatcher.destroy()
    }
}

/** --------------------------------------------------------------------------------------------- */

// TODO reconsider if we really need the ActivityBlocContext, it adds a lot of complexity

/**
 * If we're using a ViewModel, the BlocContext will be created in that ViewModel but the
 * SavedStateRegistry, ViewModelStore and OnBackPressedDispatcher are still "taken" from the
 * Activity, hence we need to pass those to the ViewModel (as ActivityBlocContext).
 */
data class ActivityBlocContext(
    val viewModelStore: ViewModelStore? = null,
    val onBackPressedDispatcher: OnBackPressedDispatcher? = null
)

/**
 * Converts an ActivityBlocContext into a BlocContext.
 * The lifecycle will be the lifecycle of the ViewModel (onCreate() and onDestroy() only)
 */
fun ViewModel.blocContext(context: ActivityBlocContext): BlocContext =
    BlocContextImpl(
        lifecycle = viewModelLifeCycle().asEssentyLifecycle(),
        instanceKeeper = context.viewModelStore?.let(::InstanceKeeper),
        backPressedHandler = context.onBackPressedDispatcher?.let(::BackPressedHandler)
    )

/**
 * To create a ViewModel "lifecycle" we create a Coroutine using the ViewModels own `viewModelScope`.
 * Upon launch the lifecycle moves to CREATED.
 * When the Coroutine is cancelled we take that as the cue to move to DESTROYED.
 *
 * Why do we do all this? Because ViewModels don't have an observable lifecycle and we'd have to
 * have a "hook" into the ViewModel's onCleared() call to create that lifecycle.
 * The ViewModel would have to extend some BaseViewModel and we don't want that.
 */
private fun ViewModel.viewModelLifeCycle(): Lifecycle = object : LifecycleOwner {
    override fun getLifecycle() = lifecycleRegistry
    private val lifecycleRegistry = LifecycleRegistry(this)

    init {
        viewModelScope.launch(Dispatchers.Main) {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
            while (isActive) {
                delay(Long.MAX_VALUE)
            }
        }.invokeOnCompletion {
            // Lifecycle.State.CREATED transitions to the stopped state...
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
    }
}.lifecycle

/**
 * From an Activity retrieve a ViewModel that takes the ActivityBlocContext as a parameter.
 */
@Suppress("UNCHECKED_CAST")
@BlocDSL
inline fun <reified VM : ViewModel> ComponentActivity.viewModel(
    crossinline createInstance: (context: ActivityBlocContext) -> VM
): Lazy<VM> {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val context = ActivityBlocContext(
                viewModelStore = viewModelStore,
                onBackPressedDispatcher = onBackPressedDispatcher
            )
            return createInstance(context) as T
        }
    }
    return ViewModelLazy(VM::class, { viewModelStore }, { factory })
}

/**
 * Same for fragments.
 */
@Suppress("UNCHECKED_CAST")
@BlocDSL
inline fun <reified VM : ViewModel> Fragment.viewModel(
    crossinline createInstance: (context: ActivityBlocContext) -> VM
): Lazy<VM> {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val context = ActivityBlocContext(
                viewModelStore = viewModelStore,
                onBackPressedDispatcher = activity?.onBackPressedDispatcher
            )
            return createInstance(context) as T
        }
    }
    return ViewModelLazy(VM::class, { viewModelStore }, { factory })
}
