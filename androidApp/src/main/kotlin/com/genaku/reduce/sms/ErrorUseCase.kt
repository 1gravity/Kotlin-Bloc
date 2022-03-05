package com.genaku.reduce.sms

import com.onegravity.knot.*
import com.onegravity.knot.state.SimpleKnotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class ErrorUseCase(dispatcher: CoroutineContext = Dispatchers.Default) : JobSwitcher, IErrorUseCase {

    private val errorKnot = simpleKnot<ErrorState, ErrorEvent> {
        dispatcher(dispatcher)

        knotState = SimpleKnotState(ErrorState.NoError)
        initialState = ErrorState.NoError

        reduce { _, intent ->
            when (intent) {
                ErrorEvent.ClearError -> ErrorState.NoError.toEffect()
                is ErrorEvent.SetError -> ErrorState.Error(intent.error).toEffect()
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