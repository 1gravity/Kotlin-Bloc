package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex

/**
 * The InitializeProcessor is responsible for processing onCreate { } blocks.
 */
internal class InitializeProcessor<State : Any, Action : Any, Proposal : Any>(
    private val blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private var initialize: Initializer<State, Action>? = null,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val dispatch: (Action) -> Unit
) {

    /**
     * Mutex to ensure that only one initializer can run at a time.
     * Since we never unlock it, it also ensures that only one initializer runs during the lifetime
     * of the bloc.
     */
    private val mutex = Mutex()

    private var coroutine: Coroutine = Coroutine(dispatcher)

    /**
     * This needs to come after all variable/property declarations to make sure everything is
     * initialized before the Bloc is started
     */
    init {
        blocContext.lifecycle.doOnCreate {
            logger.d("onCreate -> initialize Bloc")
            coroutine.onStart()
            initialize?.let { runInitializer(it) }
        }
        blocContext.lifecycle.doOnDestroy {
            logger.d("onDestroy -> destroy Bloc")
            coroutine.onStop()
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
        coroutine.scope?.launch {
            if (mutex.tryLock(this@InitializeProcessor)) {
                coroutine.runner?.let { runner ->
                    val context = InitializerContext(
                        state = blocState.value,
                        dispatch = dispatch,
                        runner = runner
                    )
                    context.initialize()
                }
            } else {
                logger.e("onCreate { } can only be run once!")
            }
        }
}
