package com.genaku.reduce.sms

import kotlinx.coroutines.flow.StateFlow

interface ILoadingUseCase : IStateUseCase {
    val loadingState: StateFlow<LoadingState>
    fun <T> processWrap(default: T, block: () -> T): T
}