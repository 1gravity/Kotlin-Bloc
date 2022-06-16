package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.bloc
import com.onegravity.bloc.runTests
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BlocExecutionTests {

    sealed class Action
    object Increment : Action()
    object Decrement : Action()

    @Test
    fun lifecycleTransitions() = runTests {
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
        bloc.send(Increment)
        delay(100)
        assertEquals(2, bloc.value)
    }

}