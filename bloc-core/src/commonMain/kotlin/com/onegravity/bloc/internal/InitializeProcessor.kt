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
    private val blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private var initialize: Initializer<State, Action>? = null,
    private val initDispatcher: CoroutineContext = Dispatchers.Default,
    private val dispatch: (Action) -> Unit
) {

    /**
     * Mutex to ensure that only one initializer can run at a time.
     * Since we never unlock it, it also ensures that only one initializer runs during the lifetime
     * of the bloc.
     */
    private val mutex = Mutex()
    private var coroutineScope = CoroutineScope(SupervisorJob() + initDispatcher)
        set(value) {
            field = value
            coroutineRunner = CoroutineRunner(coroutineScope)
        }

    private var coroutineRunner = CoroutineRunner(coroutineScope)

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        blocContext.lifecycle.doOnCreate {
            logger.d("onCreate -> initialize Bloc")
            coroutineScope = CoroutineScope(SupervisorJob() + initDispatcher)
            initialize?.let { runInitializer(it) }
        }
        blocContext.lifecycle.doOnDestroy {
            logger.d("onDestroy -> destroy Bloc")
            coroutineScope.cancel()
        }
    }

    /**
     * BlocExtension interface implementation:
     * onCreate { } -> run an initializer MVVM+ style
     */
    internal fun initialize(initialize: Initializer<State, Action>) {
        when (blocContext.lifecycle.state) {
            // if onCreate() hasn't been called yet, we can't run the initializer but we can
            // set the initializer if there isn't one yet
            Lifecycle.State.INITIALIZED -> if (this.initialize == null) this.initialize = initialize
            else -> runInitializer(initialize)
        }
    }

    private fun runInitializer(initialize: Initializer<State, Action>) =
        coroutineScope.launch {
            if (mutex.tryLock(this@InitializeProcessor)) {
                val context = InitializerContext(
                    state = blocState.value,
                    dispatch = dispatch,
                    runner = coroutineRunner
                )
                context.initialize()
            } else {
                logger.e("onCreate { } can only be run once!")
            }
        }
}
