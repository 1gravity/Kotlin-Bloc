package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BlocInitializerExecutionTests : BaseTestClass() {

    @Test
    fun testInitializerExecution1() = runTests {
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

        // will be ignored because 1) onCreate() hasn't been called yet and another initializer was
        // already defined
        bloc.onCreate { dispatch(Decrement) }
        testState(bloc, null, 1)

        lifecycleRegistry.onCreate()
        testState(bloc, null, 1)

        lifecycleRegistry.onStart()
        testState(bloc, null, 2)

        // again ignored, initializer already ran
        bloc.onCreate { dispatch(Decrement) }
        testState(bloc, null, 2)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun testInitializerExecution2() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
            reduce { state + 5 }
        }

        assertEquals(1, bloc.value)

        bloc.onCreate { dispatch(Whatever) }

        lifecycleRegistry.onCreate()
        // initializer ran but the reducers it triggered (dispatch) are still not running
        testState(bloc, null, 1)

        lifecycleRegistry.onStart()
        testState(bloc, null, 6)

        // ignored, initializer already ran
        bloc.onCreate { dispatch(Whatever) }
        testState(bloc, null, 6)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

}