package com.onegravity.knot.sample.sms

import com.onegravity.knot.*
import kotlinx.coroutines.CoroutineScope

class SmsUseCase(
    private val repository: ISmsRepository,
    private val loadingUseCase: LoadingUseCase,
    private val useCaseCoroutineScope : CoroutineScope
) : ISmsUseCase {

    private val smsKnot = knot<SmsState, SmsEvent> {
        initialState = SmsState.InputSms
        reduce { state, event ->
            when (state) {
                SmsState.InputSms -> when (event) {
                    SmsEvent.Cancel -> SmsState.Exit.toEffect()
                    is SmsEvent.SendSms -> SmsState.CheckSms + sendSms(event.sms)
                    else -> state.unexpected(event)
                }
                SmsState.CheckSms -> when (event) {
                    SmsEvent.CorrectSms -> SmsState.SmsConfirmed.toEffect()
                    SmsEvent.WrongSms -> {
                        loadingUseCase.processError(ErrorData("wrong sms"))
                        SmsState.InputSms.toEffect()
                    }
                    SmsEvent.Cancel -> SmsState.InputSms.toEffect()
                    else -> state.unexpected(event)
                }
                SmsState.Exit -> state.toEffect()
                SmsState.SmsConfirmed -> state.toEffect()
            }
        }
    }.apply {
        start(useCaseCoroutineScope)
    }

    private fun sendSms(sms: String) = SideEffect {
        if (loadingUseCase.processWrap(false) {
                repository.checkSms(sms)
            }
        ) SmsEvent.CorrectSms else SmsEvent.WrongSms
    }

    override val state: Stream<SmsState> = smsKnot

    override fun checkSms(sms: String) {
        loadingUseCase.clearError()
        smsKnot.emit(SmsEvent.SendSms(sms))
    }

    override fun cancel() {
        smsKnot.emit(SmsEvent.Cancel)
    }
}

