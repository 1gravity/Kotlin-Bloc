package com.genaku.reduce.sms

import com.onegravity.knot.*
import kotlinx.coroutines.CoroutineScope

class LoadingUseCase(private val errorUseCase: ErrorUseCase) : ILoadingUseCase,
    IErrorUseCase by errorUseCase {

    private val loadingKnot = knot<LoadingState, LoadingEvent> {
        initialState = LoadingState.Idle
        reduce { _, event ->
            when (event) {
                LoadingEvent.Start -> LoadingState.Active.toEffect()
                LoadingEvent.Stop -> LoadingState.Idle.toEffect()
            }
        }
    }

    override val loadingState: Stream<LoadingState> = loadingKnot

    override fun start(coroutineScope: CoroutineScope) {
        loadingKnot.start(coroutineScope)
        errorUseCase.start(coroutineScope)
    }

    override fun stop() {
        loadingKnot.stop()
        errorUseCase.stop()
    }

    override fun <T> processWrap(default: T, block: () -> T): T {
        loadingKnot.emit(LoadingEvent.Start)
        var result = default
        try {
            result = block()
        } catch (e: Exception) {
            errorUseCase.processError(ErrorData("error"))
        } finally {
            loadingKnot.emit(LoadingEvent.Stop)
        }
        return result
    }
}