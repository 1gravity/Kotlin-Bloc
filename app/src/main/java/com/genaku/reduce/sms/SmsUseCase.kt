package com.genaku.reduce.sms

import com.genaku.reduce.SideEffect
import com.genaku.reduce.easyKnot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import org.mym.plog.PLog

class SmsUseCase(
    private val repository: ISmsRepository,
    private val loadingUseCase: LoadingUseCase
) : ISmsUseCase, ILoadingUseCase by loadingUseCase, IErrorUseCase by loadingUseCase {

    private val smsKnot = easyKnot<SmsState, SmsIntent> {

        initialState = SmsState.InputSms

        intents { intent ->
            PLog.d("state: ${this.javaClass.simpleName} intent: ${intent.javaClass.simpleName}")
            when (this) {
                SmsState.InputSms -> when (intent) {
                    SmsIntent.Cancel -> SmsState.Exit.stateOnly
                    is SmsIntent.SendSms -> SmsState.CheckSms + sendSms(intent.sms)
                    else -> unexpected(intent)
                }
                SmsState.CheckSms -> when (intent) {
                    SmsIntent.CorrectSms -> SmsState.SmsConfirmed.stateOnly
                    SmsIntent.WrongSms -> {
                        loadingUseCase.processError(ErrorData("wrong sms"))
                        SmsState.InputSms.stateOnly
                    }
                    SmsIntent.Cancel -> SmsState.InputSms.stateOnly
                    else -> unexpected(intent)
                }
                SmsState.Exit -> {
                    this.stateOnly
                }
                SmsState.SmsConfirmed -> {
                    this.stateOnly
                }
            }
        }
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

    override fun start(scope: CoroutineScope) {
        smsKnot.start(scope)
        loadingUseCase.start(scope)
    }

    override fun stop() {
        smsKnot.stop()
        loadingUseCase.stop()
    }
}

