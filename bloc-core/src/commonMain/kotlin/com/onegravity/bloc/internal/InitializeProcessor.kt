package com.onegravity.bloc.internal

import com.onegravity.bloc.internal.lifecycle.BlocLifecycle
import com.onegravity.bloc.internal.lifecycle.subscribe
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.Initializer
import com.onegravity.bloc.utils.InitializerContext
import com.onegravity.bloc.utils.logger.logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

/**
 * The InitializeProcessor is responsible for processing onCreate { } blocks.
 */
internal class InitializeProcessor<State : Any, Action : Any, Proposal : Any>(
    private val lifecycle: BlocLifecycle,
    private val state: BlocState<State, Proposal>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private var initializer: Initializer<State, Action>? = null,
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
        lifecycle.subscribe(
            onCreate = {
                logger.d("onCreate -> initialize Bloc")
                coroutine.onStart()
                lifecycle.initializerStarting()
            },
            onInitialize = {
                logger.d("onInitialize -> run initializer")
                initializer?.apply(::runInitializer) ?: lifecycle.initializerCompleted()
            },
            onDestroy = {
                logger.d("onDestroy -> destroy Bloc")
                coroutine.onStop()
            }
        )
    }

    /**
     * BlocExtension interface implementation:
     * onCreate { } -> run an initializer MVVM+ style
     */
    internal fun initialize(initializer: Initializer<State, Action>) {
        if (this.initializer == null) {
            this.initializer = initializer
            lifecycle.initializerStarting()
        }
    }

    private fun runInitializer(initialize: Initializer<State, Action>) =
        coroutine.scope?.launch {
            if (mutex.tryLock(this@InitializeProcessor)) {
                coroutine.runner?.let { runner ->
                    val context = InitializerContext(
                        state = state.value,
                        dispatch = dispatch,
                        runner = runner
                    )
                    context.initialize()
                    lifecycle.initializerCompleted()
                }
            } else {
                logger.e("onCreate { } can only be run once!")
            }
        }

}
