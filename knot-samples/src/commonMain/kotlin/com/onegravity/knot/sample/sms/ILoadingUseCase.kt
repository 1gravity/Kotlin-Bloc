package com.onegravity.knot.sample.sms

import com.onegravity.knot.*

interface ILoadingUseCase {
    val loadingState: Stream<LoadingState>
    fun <T> processWrap(default: T, block: () -> T): T
}