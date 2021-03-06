package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.bloc
import com.onegravity.bloc.runTests
import com.onegravity.bloc.testState
import com.onegravity.bloc.thunk
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals

class BlocThunkExecutionTests : BaseTestClass() {

    private sealed class Action
    private object Increment : Action()
    private object Decrement : Action()
    private object Whatever : Action()

    @Test
    fun testThunkExecution1() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        var count = 0
        val bloc = bloc<Int, Action, Unit>(context, 1) {
            thunk<Increment> { count++ }
            thunk<Decrement> { count += 2 }
            thunk { count += 3 }
            thunk { count += 4 }
            reduce { state }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        testState(bloc, Increment, 1)

        lifecycleRegistry.onStart()
        testState(bloc, null, 1)
        assertEquals(8, count)

        testState(bloc, Decrement, 1)
        assertEquals(17, count)

        testState(bloc, Whatever, 1)
        assertEquals(24, count)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun testThunkExecution2() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        var count = 0
        val bloc = bloc<Int, Action, Unit>(context, 1) {
            thunk<Increment> {
                count++
                dispatch(Increment)
            }
            thunk<Increment> {
                count += 2
                dispatch(action)
            }
            thunk<Decrement> { count += 3 }
            thunk {
                count += 4
                dispatch(action)
            }
            thunk {
                count += 5
                dispatch(action)
            }
            reduce<Increment> { state + 1 }
            reduce { state }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        testState(bloc, Increment, 1)

        lifecycleRegistry.onStart()
        testState(bloc, null, 9)
        assertEquals(61, count)
    }

    @Test
    fun testThunkExecution3() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        var count = 0
        val bloc = bloc<Int, Action, Unit>(context, 1) {
            thunk<Increment> {
                count++
                dispatch(Decrement)
            }
            thunk<Decrement> {
                count += 2
                dispatch(Increment)
            }
            thunk {
                count += 3
                dispatch(action)
            }
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
            reduce { state + 5 }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testState(bloc, Increment, 2)
        assertEquals(12, count)

        testState(bloc, Whatever, 7)
        assertEquals(15, count)

        bloc.thunk { dispatch(Whatever) }
        delay(50)
        assertEquals(18, count)
    }

}