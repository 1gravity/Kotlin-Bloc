package com.genaku.reduce.sms

import com.genaku.reduce.Intent
import com.genaku.reduce.State

sealed class LoadingState : State {
    object Active: LoadingState()
    object Idle: LoadingState()
}

sealed class LoadingIntent : Intent {
    object Start: LoadingIntent()
    object Stop: LoadingIntent()
}