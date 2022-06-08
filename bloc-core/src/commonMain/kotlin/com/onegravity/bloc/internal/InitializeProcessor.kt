package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlin.coroutines.CoroutineContext

internal class InitializeProcessor<State : Any, Action : Any, Proposal : Any>(
    blocContext: BlocContext,
    private val blocState: BlocState<State, Proposal>,
    private val initialize: Initializer<State, Action>? = null,
    private val initDispatcher: CoroutineContext = Dispatchers.Default,
    private val dispatch: (Action) -> Unit
) {

    private val initMutex = Mutex()
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
     * Public API (interface BlocExtension) to run thunks / reducers etc. MVVM+ style
     */
    internal fun initialize(initialize: Initializer<State, Action>) =
        scope?.launch {
            if (initMutex.tryLock(this@InitializeProcessor)) {
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
