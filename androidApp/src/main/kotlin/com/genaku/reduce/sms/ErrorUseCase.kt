package com.genaku.reduce.sms

import com.onegravity.knot.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class ErrorUseCase(dispatcher: CoroutineContext = Dispatchers.Default) : JobSwitcher, IErrorUseCase {

    private val errorKnot = knot<ErrorState, ErrorIntent, Any> {
        dispatcher(dispatcher)

        initialState = ErrorState.NoError

        reduce { _, intent ->
            when (intent) {
                ErrorIntent.ClearError -> ErrorState.NoError.toEffect
                is ErrorIntent.SetError -> ErrorState.Error(intent.error).toEffect
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

    override fun start(coroutineScope: CoroutineScope) {
        errorKnot.start(coroutineScope)
    }

    override fun stop() {
        errorKnot.stop()
    }
}