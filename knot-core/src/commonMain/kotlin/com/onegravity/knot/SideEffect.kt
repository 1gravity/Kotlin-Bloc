package com.onegravity.knot

import kotlinx.coroutines.CoroutineScope

data class SideEffect<out Event>(val block: (coroutineScope: CoroutineScope) -> Event?)
