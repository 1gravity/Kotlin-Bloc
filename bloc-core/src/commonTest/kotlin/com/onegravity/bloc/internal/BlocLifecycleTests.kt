package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.bloc
import com.onegravity.bloc.runTests
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BlocLifecycleTests {

    @Test
    fun lifecycleTransitions() = runTests {
        LifecycleRegistry().legalTransition { onCreate() }
        LifecycleRegistry().illegalTransition { onStart() }
        LifecycleRegistry().illegalTransition { onResume() }
        LifecycleRegistry().illegalTransition { onStop() }
        LifecycleRegistry().illegalTransition { onDestroy() }

        LifecycleRegistry().legalTransition {
            onCreate()
            onStart()
        }
        LifecycleRegistry().legalTransition {
            onCreate()
            onStart()
            onResume()
        }
        LifecycleRegistry().illegalTransition {
            onCreate()
            onResume()
        }
        LifecycleRegistry().legalTransition {
            onCreate()
            onDestroy()
        }
        LifecycleRegistry().illegalTransition {
            onCreate()
            onStart()
            onResume()
            onStop()
        }
        LifecycleRegistry().legalTransition {
            onCreate()
            onStart()
            onResume()
            onPause()
            onStop()
        }
        LifecycleRegistry().illegalTransition {
            onCreate()
            onDestroy()
            onStart()
        }
    }

    private fun LifecycleRegistry.legalTransition(transition: LifecycleRegistry.() -> Unit) {
        var exception: Exception? = null
        try {
            transition()
        } catch (e: Exception) {
            exception = e
        }
        assertEquals(null, exception)
    }

    private fun LifecycleRegistry.illegalTransition(transition: LifecycleRegistry.() -> Unit) {
        var exception: Exception? = null
        try {
            transition()
        } catch (e: Exception) {
            exception = e
        }
        assertNotEquals(null, exception)
    }

    @Test
    fun reducerLifecycleTest() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Int>(context, 1) {
            reduce { state + action }
        }

        assertEquals(1, bloc.value)

        bloc.send(1)
        delay(100)
        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        assertEquals(1, bloc.value)

        lifecycleRegistry.onStart()
        delay(100)
        assertEquals(2, bloc.value)

        lifecycleRegistry.onStop()
        bloc.send(1)
        delay(100)
        assertEquals(2, bloc.value)

        lifecycleRegistry.onStart()
        delay(100)
        assertEquals(3, bloc.value)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
        bloc.send(1)
        delay(100)
        assertEquals(3, bloc.value)
    }

    @Test
    fun thunkLifecycleTest() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Int>(context, 1) {
            thunk {
                dispatch(action + 1)
            }
            reduce { state + action }
        }

        assertEquals(1, bloc.value)

        bloc.send(1)
        delay(100)
        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        assertEquals(1, bloc.value)

        lifecycleRegistry.onStart()
        delay(100)
        assertEquals(3, bloc.value)

        lifecycleRegistry.onStop()
        bloc.send(1)
        delay(100)
        assertEquals(3, bloc.value)

        lifecycleRegistry.onStart()
        delay(100)
        assertEquals(5, bloc.value)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
        bloc.send(1)
        delay(100)
        assertEquals(5, bloc.value)
    }

    @Test
    fun initializerLifecycleTest() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Int>(context, 1) {
            onCreate {
                dispatch(7)
            }
            reduce { state + action }
        }

        assertEquals(1, bloc.value)

        lifecycleRegistry.onCreate()
        delay(100)
        assertEquals(1, bloc.value)

        lifecycleRegistry.onStart()
        delay(100)
        assertEquals(8, bloc.value)

        lifecycleRegistry.onStop()
        bloc.send(1)
        delay(100)
        assertEquals(8, bloc.value)

        lifecycleRegistry.onStart()
        delay(100)
        assertEquals(9, bloc.value)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
        bloc.send(1)
        delay(100)
        assertEquals(9, bloc.value)
    }
}