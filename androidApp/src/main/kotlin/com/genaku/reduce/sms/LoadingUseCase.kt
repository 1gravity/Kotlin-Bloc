package com.genaku.reduce.sms

import com.onegravity.knot.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.mym.plog.PLog

class LoadingUseCase(private val errorUseCase: ErrorUseCase) : ILoadingUseCase,
    IErrorUseCase by errorUseCase {

    private val loadingKnot = knot<LoadingState, LoadingIntent, Any> {
        initialState = LoadingState.Idle

        reduce { _, intent ->
            PLog.d( "loading intent $intent")
            when (intent) {
                LoadingIntent.Start -> LoadingState.Active.toEffect
                LoadingIntent.Stop -> LoadingState.Idle.toEffect
            }
        }
    }

    override val loadingState: StateFlow<LoadingState>
        get() = loadingKnot.state

    override fun start(coroutineScope: CoroutineScope) {
        PLog.d( "start loading use case")
        loadingKnot.start(coroutineScope)
        errorUseCase.start(coroutineScope)
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