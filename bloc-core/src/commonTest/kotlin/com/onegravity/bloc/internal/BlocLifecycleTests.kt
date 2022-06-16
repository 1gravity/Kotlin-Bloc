package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.bloc
import com.onegravity.bloc.runTests
import com.onegravity.bloc.testBloc
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

        testBloc(bloc, 1, 1)

        lifecycleRegistry.onCreate()
        assertEquals(1, bloc.value)

        lifecycleRegistry.onStart()
        testBloc(bloc, null, 2)

        lifecycleRegistry.onStop()
        testBloc(bloc, 1, 2)

        lifecycleRegistry.onStart()
        testBloc(bloc, null, 3)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()

        testBloc(bloc, 1, 3)
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

        testBloc(bloc, 1, 1)

        lifecycleRegistry.onCreate()
        assertEquals(1, bloc.value)

        lifecycleRegistry.onStart()
        testBloc(bloc, null, 3)

        lifecycleRegistry.onStop()
        testBloc(bloc, 1, 3)

        lifecycleRegistry.onStart()
        testBloc(bloc, null, 5)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()

        testBloc(bloc, 1, 5)
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
        testBloc(bloc, null, 1)

        lifecycleRegistry.onStart()
        testBloc(bloc, null, 8)

        lifecycleRegistry.onStop()
        testBloc(bloc, 1, 8)

        lifecycleRegistry.onStart()
        testBloc(bloc, null, 9)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()

        testBloc(bloc, 1, 9)
    }
}