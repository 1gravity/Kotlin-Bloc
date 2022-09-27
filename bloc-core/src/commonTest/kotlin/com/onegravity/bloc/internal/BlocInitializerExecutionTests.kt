@file:Suppress("WildcardImport")

package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.bloc
import com.onegravity.bloc.reduce
import com.onegravity.bloc.runTests
import com.onegravity.bloc.testCollectState
import com.onegravity.bloc.testState
import com.onegravity.bloc.thunk
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals

class BlocInitializerExecutionTests : BaseTestClass() {

    /**
     * Test regular initializer and make sure MVVM+ style initializer don't run
     */
    @Test
    fun testInitializerExecutionReduxStyle() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> { state + 1 }
            onCreate { dispatch(Increment) }
            reduce<Decrement> { state - 1 }
            reduce { state + 5 }
            onCreate { dispatch(Decrement) }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        testState(bloc, null, 1)

        lifecycleRegistry.onStart()
        testState(bloc, null, 2)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    /**
     * Test whether the bloc waits for the initializer to finish before transitioning to started
     * which will start processing dispatched actions.
     */
    @Test
    fun testInitializerExecutionDelayed() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 5) {
            onCreate {
                delay(1000)
                dispatch(Increment)
            }
            reduce<Increment> { state + 1 }
            reduce { state + 5 }
        }

        assertEquals(5, bloc.value)

        lifecycleRegistry.onCreate()
        assertEquals(5, bloc.value)

        // the initializer is still running -> the dispatched action has no effect
        lifecycleRegistry.onStart()
        testState(bloc, Whatever, 5)

        delay(1050)
        assertEquals(11, bloc.value)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    /**
     * Test whether long running initializers still run before everything else
     */
    @Test
    fun testInitializerExecutionOrder() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 5) {
            onCreate {
                delay(1000)
                dispatch(Increment)
            }
            thunk<Increment> {
                dispatch(Whatever)
            }
            thunk<Decrement> {
                dispatch(Decrement)
            }
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
            reduce { state + 5 }
        }

        assertEquals(5, bloc.value)

        // the bloc needs to wait for the initializer to finish before processing the sent action
        testCollectState(
            bloc,
            listOf(5, 10, 15, 14, 19)
        ) {
            lifecycleRegistry.onCreate()
            lifecycleRegistry.onStart()
            bloc.send(Whatever)
            bloc.send(Decrement)
            bloc.send(Increment)
            delay(1050)
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    /**
     * Test whether long running initializers still run before everything else,
     * now for MVVM+ style reducers and thunks
     */
    @Test
    fun testInitializerExecutionOrderMVVM() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 5) {
            onCreate {
                delay(1000)
                dispatch(Increment)
            }
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
            reduce { state + 5 }
        }

        assertEquals(5, bloc.value)

        // the bloc needs to wait for the initializer to finish before processing the sent action
        testCollectState(
            bloc,
            listOf(5, 6, 5, 15, 20, 19, 24, 25)
        ) {
            lifecycleRegistry.onCreate()
            lifecycleRegistry.onStart()
            bloc.reduce { state - 1 }
            bloc.reduce { state + 10 }
            bloc.reduce { state + 5 }
            bloc.thunk { dispatch(Decrement) }
            bloc.thunk {
                delay(100)
                dispatch(Whatever)
            }
            // we need >1000ms here because 1000ms pass due the initializer and 100ms in the last thunk
            delay(1200)
            bloc.reduce { state + 1 }
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

}
