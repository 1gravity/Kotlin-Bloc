package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlin.coroutines.CoroutineContext

/**
 * The InitializeProcessor is responsible for processing onCreate { } blocks.
 */
internal class InitializeProcessor<State : Any, Action : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val initialize: Initializer<State, Action>? = null,
    private val initDispatcher: CoroutineContext = Dispatchers.Default,
    private val dispatch: (Action) -> Unit
) {

    /**
     * Mutex to ensure that only one initializer can run at a time.
     * Since we never unlock it, it also ensures that only one initializer runs during the lifetime
     * of the bloc.
     */
    private val mutex = Mutex()
    private var scope: CoroutineScope? = null

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        blocContext.lifecycle.doOnCreate {
            logger.d("onCreate -> initialize Bloc")
            scope = CoroutineScope(SupervisorJob() + initDispatcher)
            scope?.launch {
                initialize?.let { initialize(it) }
            }
        }
        blocContext.lifecycle.doOnDestroy {
            logger.d("onDestroy -> destroy Bloc")
            scope?.cancel()
        }
    }

    /**
     * BlocExtension interface implementation:
     * onCreate { } -> run an initializer MVVM+ style
     */
    internal fun initialize(initialize: Initializer<State, Action>) =
        scope?.launch {
            if (mutex.tryLock(this@InitializeProcessor)) {
                val context = InitializerContext(
                    state = blocState.value,
                    coroutineScope = this,
                    dispatch = dispatch
                )
                context.initialize()
            } else {
                logger.e("onCreate { } can only be run once!")
            }
        }

}
