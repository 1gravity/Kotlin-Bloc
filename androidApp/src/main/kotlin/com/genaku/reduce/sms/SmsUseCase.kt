package com.genaku.reduce.sms

import com.onegravity.knot.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.mym.plog.PLog

class SmsUseCase(
    private val repository: ISmsRepository,
    private val loadingUseCase: LoadingUseCase,
    private val useCaseCoroutineScope : CoroutineScope
) : ISmsUseCase {

    private val smsKnot = easyKnot<SmsState, SmsIntent> {

        initialState = SmsState.InputSms

        reduce { state, intent ->
            PLog.d("state: ${javaClass.simpleName} intent: ${intent.javaClass.simpleName}")
            when (state) {
                SmsState.InputSms -> when (intent) {
                    SmsIntent.Cancel -> SmsState.Exit.toEffect
                    is SmsIntent.SendSms -> SmsState.CheckSms + sendSms(intent.sms)
                    else -> state.unexpected(intent)
                }
                SmsState.CheckSms -> when (intent) {
                    SmsIntent.CorrectSms -> SmsState.SmsConfirmed.toEffect
                    SmsIntent.WrongSms -> {
                        loadingUseCase.processError(ErrorData("wrong sms"))
                        SmsState.InputSms.toEffect
                    }
                    SmsIntent.Cancel -> SmsState.InputSms.toEffect
                    else -> state.unexpected(intent)
                }
                SmsState.Exit -> state.toEffect
                SmsState.SmsConfirmed -> state.toEffect
            }
        }
    }.apply {
        start(useCaseCoroutineScope)
    }

    private fun sendSms(sms: String) = SideEffect {
        if (loadingUseCase.processWrap(false) {
                repository.checkSms(sms)
            }
        ) SmsIntent.CorrectSms else SmsIntent.WrongSms
    }

    override val state: StateFlow<SmsState>
        get() = smsKnot.state

    override fun checkSms(sms: String) {
        loadingUseCase.clearError()
        smsKnot.offerIntent(SmsIntent.SendSms(sms))
    }

    override fun cancel() {
        smsKnot.offerIntent(SmsIntent.Cancel)
    }
}

