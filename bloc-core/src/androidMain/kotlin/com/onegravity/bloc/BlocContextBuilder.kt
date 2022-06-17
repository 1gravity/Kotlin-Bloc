/**
 * From https://github.com/arkivanov/Decompose
 */

@file:Suppress("SpellCheckingInspection")

package com.onegravity.bloc

import androidx.lifecycle.*
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.onegravity.bloc.internal.BlocContextImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/** --------------------------------------------------------------------------------------------- */
/** Activity / Fragment                                                                           */
/** --------------------------------------------------------------------------------------------- */

/**
 * Use this from an Activity or a Fragment to get or create a "Component" without directly involving
 * a ViewModel, e.g.:
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
inline fun <A: ViewModelStoreOwner, reified Component : Any> A.getOrCreate(
    key: Any = Component::class,
    noinline create: (context: BlocContext) -> Component
): Lazy<Component> = ComponentLazy(
    owner = lazy { this },
    key = key,
    create = create
)

/** --------------------------------------------------------------------------------------------- */
/** ViewModel                                                                                     */
/** --------------------------------------------------------------------------------------------- */

/**
 * Create a BlocContext from a ViewModel.
 * The lifecycle will be the "lifecycle" of the ViewModel.
 */
fun ViewModel.blocContext(): BlocContext =
    BlocContextImpl(
        lifecycle = viewModelLifeCycle().asEssentyLifecycle()
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
            lifecycleRegistry.currentState = Lifecycle.State.CREATED    // triggers onCreate()
            lifecycleRegistry.currentState = Lifecycle.State.STARTED    // triggers onStart()
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED    // triggers onResume()
            while (isActive) {
                delay(Long.MAX_VALUE)
            }
        }.invokeOnCompletion {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED    // triggers onStop()
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED  // triggers onDestroy()
        }
    }
}.lifecycle
