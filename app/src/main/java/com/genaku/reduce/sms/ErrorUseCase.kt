package com.genaku.reduce.sms

import com.genaku.reduce.StateAction
import com.genaku.reduce.knot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class ErrorUseCase : IStateUseCase, IErrorUseCase {

    private val errorKnot = knot<ErrorState, ErrorIntent, StateAction> {
        initialState = ErrorState.NoError

        reduce { intent ->
            when (intent) {
                ErrorIntent.ClearError -> ErrorState.NoError.stateOnly
                is ErrorIntent.SetError -> ErrorState.Error(intent.error).stateOnly
            }
        }
    }

    override val errorState: StateFlow<ErrorState>
        get() = errorKnot.state

    override fun processError(error: IError) {
        errorKnot.offerIntent(ErrorIntent.SetError(error))
    }

    override fun clearError() {
        errorKnot.offerIntent(ErrorIntent.ClearError)
    }

    override fun start(scope: CoroutineScope) {
        errorKnot.start(scope)
    }

    override fun stop() {
        errorKnot.stop()
    }
}