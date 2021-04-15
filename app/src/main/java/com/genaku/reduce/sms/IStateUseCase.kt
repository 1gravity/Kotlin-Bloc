package com.genaku.reduce.sms

import kotlinx.coroutines.CoroutineScope

interface IStateUseCase {
    fun start(scope: CoroutineScope)
    fun stop()
}