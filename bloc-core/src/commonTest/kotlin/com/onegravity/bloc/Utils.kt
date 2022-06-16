package com.onegravity.bloc

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

fun runTests(block: suspend () -> Unit) =
    runTest {
        withContext(Dispatchers.Default) {
            block()
        }
    }

