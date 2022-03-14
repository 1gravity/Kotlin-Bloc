package com.onegravity.knot.sample.sms

import com.onegravity.bloc.Stream
import com.onegravity.knot.*
import com.onegravity.bloc.context.BlocContext

class ErrorUseCase(context: BlocContext) : IErrorUseCase {

    private val errorKnot = knot<ErrorState, ErrorEvent>(context, ErrorState.NoError) {
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