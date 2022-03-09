package com.onegravity.knot.sample.sms

import com.onegravity.knot.*

interface ILoadingUseCase : JobSwitcher {
    val loadingState: Stream<LoadingState>
    fun <T> processWrap(default: T, block: () -> T): T
}