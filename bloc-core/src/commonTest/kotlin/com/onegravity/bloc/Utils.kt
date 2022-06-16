package com.onegravity.bloc

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.assertEquals

fun runTests(block: suspend () -> Unit) =
    runTest {
        withContext(Dispatchers.Default) {
            block()
        }
    }

suspend fun <State : Any, Action : Any, SideEffect: Any> testBloc(
    bloc: Bloc<State, Action, SideEffect>,
    action: Action?,
    expected: State
) {
    if (action != null) bloc.send(action)
    delay(100)
    assertEquals(expected, bloc.value)
}
