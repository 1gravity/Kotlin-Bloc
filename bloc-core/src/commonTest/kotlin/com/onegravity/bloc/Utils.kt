package com.onegravity.bloc

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import kotlin.test.assertEquals

/**
 * runTest() ignores all delay() calls so most tests would fail since they test asynchronous code
 */
fun runTests(block: suspend () -> Unit) =
    runTest {
        withContext(Dispatchers.Default) {
            block()
        }
    }

suspend fun <Value: Any> StateStream<Value>.toFlow(): Flow<Value> {
    val flow = MutableStateFlow(value)
    collect {
        flow.emit(it)
    }
    return flow
}

suspend fun <State : Any, Action : Any, SideEffect: Any> testCollectState(
    bloc: Bloc<State, Action, SideEffect>,
    expected: List<State>,
    block: suspend () -> Unit
) {
    val (job, values) = collectState(bloc)

    withContext(Dispatchers.Default) {
        delay(100)
        block()
        delay(100)
        job.cancel()
    }

    assertEquals(expected, values)
}

fun <State : Any, Action : Any, SideEffect: Any> collectState(
    bloc: Bloc<State, Action, SideEffect>,
): Pair<Job, MutableList<State>> {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val values = mutableListOf<State>()
    val job = coroutineScope.launch {
        bloc.collect {
            values.add(it)
        }
    }
    return Pair(job, values)
}

suspend fun <State : Any, Action : Any, SideEffect: Any> testCollectSideEffects(
    bloc: Bloc<State, Action, SideEffect>,
    expected: List<SideEffect>,
    block: suspend () -> Unit
) {
    val (job, values) = collectSideEffects(bloc)

    withContext(Dispatchers.Default) {
        delay(100)
        block()
        delay(100)
        job.cancel()
    }

    assertEquals(expected, values)
}

fun <State : Any, Action : Any, SideEffect: Any> collectSideEffects(
    bloc: Bloc<State, Action, SideEffect>,
): Pair<Job, MutableList<SideEffect>> {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val values = mutableListOf<SideEffect>()
    val job = coroutineScope.launch {
        bloc.sideEffects.collect {
            values.add(it)
        }
    }
    return Pair(job, values)
}

suspend fun <State : Any, Action : Any, SideEffect: Any> testState(
    bloc: Bloc<State, Action, SideEffect>,
    action: Action?,
    expected: State
) {
    if (action != null) bloc.send(action)
    delay(100)
    assertEquals(expected, bloc.value)
}
