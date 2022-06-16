package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BlocReducerExecutionTests {

    sealed class Action
    object Increment : Action()
    object Decrement : Action()
    object Whatever : Action()

    @Test
    fun testReducerExecution() = runTests {
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
        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testBloc(bloc, Increment, 2)

        testBloc(bloc, Decrement, 1)

        testBloc(bloc, Whatever, 6)

        bloc.reduce {
            state -4
        }
        testBloc(bloc, null, 2)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

}