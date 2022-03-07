package com.genaku.reduce.sms

import com.onegravity.knot.*
import kotlinx.coroutines.CoroutineScope

class ErrorUseCase() : JobSwitcher, IErrorUseCase {

    private val errorKnot = knot<ErrorState, ErrorEvent> {
        initialState = ErrorState.NoError
        reduce { _, event ->
            when (event) {
                ErrorEvent.ClearError -> ErrorState.NoError.toEffect()
                is ErrorEvent.SetError -> ErrorState.Error(event.error).toEffect()
            }
        }
    }

    override val errorState: Stream<ErrorState> = errorKnot

    override fun processError(error: IError) {
        errorKnot.emit(ErrorEvent.SetError(error))
    }

    override fun clearError() {
        errorKnot.emit(ErrorEvent.ClearError)
    }

    override fun start(coroutineScope: CoroutineScope) {
        errorKnot.start(coroutineScope)
    }

    override fun stop() {
        errorKnot.stop()
    }
}