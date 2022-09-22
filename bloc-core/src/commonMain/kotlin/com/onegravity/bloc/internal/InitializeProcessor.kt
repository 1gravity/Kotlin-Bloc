package com.onegravity.bloc.internal

import com.onegravity.bloc.internal.lifecycle.BlocLifecycle
import com.onegravity.bloc.internal.lifecycle.doOnCreate
import com.onegravity.bloc.internal.lifecycle.doOnDestroy
import com.onegravity.bloc.internal.lifecycle.doOnInitialize
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.Initializer
import com.onegravity.bloc.utils.InitializerContext
import com.onegravity.bloc.utils.logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

/**
 * The InitializeProcessor is responsible for processing onCreate { } blocks.
 */
internal class InitializeProcessor<State : Any, Action : Any, Proposal : Any>(
    private val blocLifecycle: BlocLifecycle,
    private val blocState: BlocState<State, Proposal>,
    private var initializer: Initializer<State, Action>? = null,
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
        blocLifecycle.doOnCreate {
            logger.d("onCreate -> initialize Bloc")
            coroutine.onStart()
            initializer?.run { blocLifecycle.initializerStarting() }

        }
        blocLifecycle.doOnInitialize {
            logger.d("doOnInitialize -> run initializer")
            initializer?.run { runInitializer(this) }
        }
        blocLifecycle.doOnDestroy {
            logger.d("onDestroy -> destroy Bloc")
            coroutine.onStop()
        }
    }

    /**
     * BlocExtension interface implementation:
     * onCreate { } -> run an initializer MVVM+ style
     */
    internal fun initialize(initializer: Initializer<State, Action>) {
        if (this.initializer == null) {
            this.initializer = initializer
            blocLifecycle.initializerStarting()
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
                    blocLifecycle.initializerCompleted()
                }
            } else {
                logger.e("onCreate { } can only be run once!")
            }
        }
}
