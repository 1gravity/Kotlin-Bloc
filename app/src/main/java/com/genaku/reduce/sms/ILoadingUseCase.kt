package com.genaku.reduce.sms

import com.genaku.reduce.JobSwitcher
import kotlinx.coroutines.flow.StateFlow

interface ILoadingUseCase : JobSwitcher {
    val loadingState: StateFlow<LoadingState>
    fun <T> processWrap(default: T, block: () -> T): T
}