package com.genaku.reduce.sms

import com.genaku.reduce.Action
import com.genaku.reduce.knot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.mym.plog.PLog

class LoadingUseCase(private val errorUseCase: ErrorUseCase) : ILoadingUseCase,
    IErrorUseCase by errorUseCase {

    private val loadingKnot = knot<LoadingState, LoadingIntent, Action> {
        initialState = LoadingState.Idle

        intents { intent ->
            PLog.d( "loading intent $intent")
            when (intent) {
                LoadingIntent.Start -> LoadingState.Active.stateOnly
                LoadingIntent.Stop -> LoadingState.Idle.stateOnly
            }
        }
    }

    override val loadingState: StateFlow<LoadingState>
        get() = loadingKnot.state

    override fun start(scope: CoroutineScope) {
        PLog.d( "start loading use case")
        loadingKnot.start(scope)
        errorUseCase.start(scope)
    }

    override fun stop() {
        PLog.d( "stop loading use case")
        loadingKnot.stop()
        errorUseCase.stop()
    }

    override fun <T> processWrap(default: T, block: () -> T): T {
        PLog.d( "wrap")
        loadingKnot.offerIntent(LoadingIntent.Start)
        var result = default
        try {
            result = block()
        } catch (e: Exception) {
            errorUseCase.processError(ErrorData("error"))
        } finally {
            loadingKnot.offerIntent(LoadingIntent.Stop)
        }
        return result
    }
}