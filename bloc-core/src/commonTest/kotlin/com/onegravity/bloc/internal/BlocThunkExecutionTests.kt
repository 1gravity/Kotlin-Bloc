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

    @Test
    fun `test thunk execution - lifecycle - order of execution - matching`() = runTests {
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

        // action is sent before the bloc is started -> no effect
        lifecycleRegistry.onCreate()
        testState(bloc, Increment, 1)
        lifecycleRegistry.onStart()
        delay(50)
        assertEquals(0, count)
        testState(bloc, null, 1)

        // thunk 2, 3 and 4 are executed
        testState(bloc, Decrement, 1)
        assertEquals(9, count)

        testState(bloc, Whatever, 1)
        assertEquals(16, count)

        // even if we restart the bloc, actions that were sent when it was stopped, are dropped
        lifecycleRegistry.onStop()
        testState(bloc, Whatever, 1)
        testState(bloc, Whatever, 1)
        testState(bloc, Whatever, 1)
        lifecycleRegistry.onStart()
        assertEquals(1, bloc.value)
        assertEquals(16, count)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test thunk execution - with dispatch`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        var count = 0
        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> { state + 1 }
            reduce { state }
            thunk<Increment> { // + 4
                count++
                dispatch(Increment)
            }
            thunk<Increment> { // + 2
                count += 2
                dispatch(action)
            }
            thunk<Decrement> { count += 3 }
            thunk { // + 1
                count += 4
                dispatch(action)
            }
            thunk { // + 1
                count += 5
                dispatch(action)
            }
        }

        assertEquals(1, bloc.value)

        // action is sent before the bloc is started -> no effect
        lifecycleRegistry.onCreate()
        testState(bloc, Increment, 1)
        lifecycleRegistry.onStart()
        testState(bloc, null, 1)
        assertEquals(0, count)

        testState(bloc, Increment, 9)
        assertEquals(61, count)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test thunk execution - builder and MVVM+ style`() = runTests {
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

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test thunk execution - getState`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            thunk<Increment> {
                reduce(getState() + 1)
            }
            thunk<Decrement> {
                reduce(getState() - 1)
                dispatch(Decrement)
            }
            thunk {
                dispatch(action)
            }
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
            reduce { state + 5 }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testState(bloc, Increment, 3)

        testState(bloc, Whatever, 8)

        testState(bloc, Decrement, 5)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test thunk execution - with getState and dispatch`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            thunk<Increment> {
                repeat(3) {
                    val state = getState()
                    dispatch(Increment)
                    assertEquals(state + 1, getState())
                    dispatch(Decrement)
                    assertEquals(state, getState())
                    reduce(getState() + 1)
                    assertEquals(state + 1, getState())
                    reduce(getState() - 1)
                    assertEquals(state, getState())
                    reduce(getState() + 2)
                    assertEquals(state + 2, getState())
                }
            }
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testState(bloc, Increment, 7)
        testState(bloc, Increment, 13)
        testState(bloc, Increment, 19)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test thunk execution - with getState and dispatch - MVVM+ style`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        bloc.thunk {
            val state = getState()
            dispatch(Increment)
            assertEquals(state + 1, getState())
            dispatch(Decrement)
            assertEquals(state, getState())
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }
    
    @Test
    fun `test thunk execution - with getState and dispatch and reduce - MVVM+ style`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        bloc.thunk {
            val state = getState()
            dispatch(Increment)
            assertEquals(state + 1, getState())
            dispatch(Decrement)
            assertEquals(state, getState())
            reduce(getState() + 1)
            assertEquals(state + 1, getState())
            reduce(getState() - 1)
            assertEquals(state, getState())
            reduce(getState() + 2)
            assertEquals(state + 2, getState())
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

}
