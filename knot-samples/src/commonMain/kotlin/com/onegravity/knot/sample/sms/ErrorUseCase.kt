package com.onegravity.knot.sample.sms

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext

class ErrorUseCase(context: KnotContext) : IErrorUseCase {

    private val errorKnot = knot<ErrorState, ErrorEvent>(context) {
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

}