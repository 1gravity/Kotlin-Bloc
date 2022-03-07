package com.genaku.reduce.sms

sealed class LoadingState {
    object Active: LoadingState()
    object Idle: LoadingState()
}

sealed class LoadingEvent {
    object Start: LoadingEvent()
    object Stop: LoadingEvent()
}