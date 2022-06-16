package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BlocInitializerExecutionTests {

    sealed class Action
    object Increment : Action()
    object Decrement : Action()
    object Whatever : Action()

    @Test
    fun testInitializerExecution1() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> {
                state + 1
            }
            onCreate {
                dispatch(Increment)
            }
            reduce<Decrement> {
                state - 1
            }
            reduce {
                state + 5
            }
            onCreate {
                dispatch(Decrement)
            }
        }

        assertEquals(1, bloc.value)

        // will be ignored because 1) onCreate() hasn't been called yet and another initializer was
        // already defined
        bloc.onCreate<Int, Action, Unit, Int> { dispatch(Decrement) }
        testBloc(bloc, null, 1)

        lifecycleRegistry.onCreate()
        testBloc(bloc, null, 1)

        lifecycleRegistry.onStart()
        testBloc(bloc, null, 2)

        // again ignored, initializer already ran
        bloc.onCreate<Int, Action, Unit, Int> { dispatch(Decrement) }
        testBloc(bloc, null, 2)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun testInitializerExecution2() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> {
                state + 1
            }
            reduce<Decrement> {
                state - 1
            }
            reduce {
                state + 5
            }
        }

        assertEquals(1, bloc.value)

        bloc.onCreate<Int, Action, Unit, Int> { dispatch(Whatever) }

        lifecycleRegistry.onCreate()
        // initializer ran but the reducers it triggered (dispatch) are still not running
        testBloc(bloc, null, 1)

        lifecycleRegistry.onStart()
        testBloc(bloc, null, 6)

        // ignored, initializer already ran
        bloc.onCreate<Int, Action, Unit, Int> { dispatch(Whatever) }
        testBloc(bloc, null, 6)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

}