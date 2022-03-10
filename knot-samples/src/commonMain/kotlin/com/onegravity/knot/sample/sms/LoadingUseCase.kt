package com.onegravity.knot.sample.sms

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext

class LoadingUseCase(context: KnotContext, private val errorUseCase: ErrorUseCase) :
    ILoadingUseCase,
    IErrorUseCase by errorUseCase {

    private val loadingKnot = knot<LoadingState, LoadingEvent>(context, LoadingState.Idle) {
        reduce { _, event ->
            when (event) {
                LoadingEvent.Start -> LoadingState.Active.toEffect()
                LoadingEvent.Stop -> LoadingState.Idle.toEffect()
            }
        }
    }

    override val loadingState: Stream<LoadingState> = loadingKnot

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