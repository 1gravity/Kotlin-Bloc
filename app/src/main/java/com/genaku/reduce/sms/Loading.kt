package com.genaku.reduce.sms

import com.genaku.reduce.StateIntent
import com.genaku.reduce.State

sealed class LoadingState : State {
    object Active: LoadingState()
    object Idle: LoadingState()
}

sealed class LoadingIntent : StateIntent {
    object Start: LoadingIntent()
    object Stop: LoadingIntent()
}