package com.onegravity.knot.context

import kotlinx.coroutines.CoroutineScope

interface CoroutineScopeOwner {

    val coroutineScope: CoroutineScope

}
