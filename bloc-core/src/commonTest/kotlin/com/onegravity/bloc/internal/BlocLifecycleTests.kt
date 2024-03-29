@file:Suppress("WildcardImport")

package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.bloc
import com.onegravity.bloc.reduce
import com.onegravity.bloc.runTests
import com.onegravity.bloc.testState
import com.onegravity.bloc.thunk
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BlocLifecycleTests : BaseTestClass() {

    /**
     * Testing the Essenty lifecycle
     */
    @Test
    fun `test Essenty lifecycle transitions`() = runTests {
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
    fun `test bloc lifecycle with reducer`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Int>(context, 1) {
            reduce { state + action }
        }

        assertEquals(1, bloc.value)

        testState(bloc, 1, 1)

        lifecycleRegistry.onCreate()
        assertEquals(1, bloc.value)

        lifecycleRegistry.onStart()
        testState(bloc, null, 1)
        assertEquals(1, bloc.value)
        testState(bloc, 5, 6)

        lifecycleRegistry.onStop()
        testState(bloc, 1, 6)

        bloc.reduce { state + 5 }
        delay(10)
        assertEquals(6, bloc.value)

        lifecycleRegistry.onStart()
        lifecycleRegistry.onStop()
        lifecycleRegistry.onStart()
        lifecycleRegistry.onStop()
        lifecycleRegistry.onStart()

        testState(bloc, null, 6)
        testState(bloc, 1, 7)

        bloc.reduce { state + 5 }
        delay(10)
        assertEquals(12, bloc.value)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()

        testState(bloc, 1, 12)
    }

    @Test
    fun `test bloc lifecycle with thunk`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Int>(context, 1) {
            thunk {
                dispatch(action + 1)
            }
            reduce { state + action }
        }

        assertEquals(1, bloc.value)

        testState(bloc, 1, 1)

        lifecycleRegistry.onCreate()
        assertEquals(1, bloc.value)

        lifecycleRegistry.onStart()
        lifecycleRegistry.onStop()
        lifecycleRegistry.onStart()
        lifecycleRegistry.onStop()
        lifecycleRegistry.onStart()

        testState(bloc, null, 1)
        testState(bloc, 1, 3)

        bloc.thunk { dispatch(1) }
        delay(10)
        assertEquals(5, bloc.value)

        lifecycleRegistry.onStop()
        testState(bloc, 1, 5)

        bloc.thunk { dispatch(1) }
        delay(10)
        assertEquals(5, bloc.value)

        lifecycleRegistry.onStart()
        testState(bloc, null, 5)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()

        testState(bloc, 1, 5)
    }

    @Suppress("RemoveExplicitTypeArguments")
    @Test
    fun `test bloc lifecycle with initializer`() = runTests {
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
        delay(10)
        assertEquals(8, bloc.value)
        testState(bloc, null, 8)

        lifecycleRegistry.onStart()
        testState(bloc, null, 8)

        lifecycleRegistry.onStop()
        testState(bloc, 1, 8)

        lifecycleRegistry.onStart()
        lifecycleRegistry.onStop()
        lifecycleRegistry.onStart()
        lifecycleRegistry.onStop()
        lifecycleRegistry.onStart()

        testState(bloc, null, 8)
        testState(bloc, 1, 9)
        testState(bloc, 1, 10)

        lifecycleRegistry.onStop()
        testState(bloc, 1, 10)

        lifecycleRegistry.onDestroy()
        testState(bloc, 1, 10)
    }

    @Suppress("RemoveExplicitTypeArguments")
    @Test
    fun `test bloc lifecycle started early with initializer`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Int>(context, 1) {
            onCreate { dispatch(7) }
            reduce { state + action }
        }

        delay(10)
        assertEquals(8, bloc.value)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Suppress("RemoveExplicitTypeArguments")
    @Test
    fun `test bloc lifecycle started and stopped early with initializer`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()
        lifecycleRegistry.onStop()

        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Int>(context, 1) {
            onCreate { dispatch(7) }
            reduce { state + action }
        }

        delay(10)
        assertEquals(8, bloc.value)

        // this won't have an effect because the bloc is already stopped
        testState(bloc, 1, 8)

        lifecycleRegistry.onDestroy()
    }

}
