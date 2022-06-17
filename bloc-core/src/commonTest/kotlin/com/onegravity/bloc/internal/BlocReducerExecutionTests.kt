package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
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
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
            reduce { state + 5 }
        }

        assertEquals(1, bloc.value)
        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testState(bloc, Increment, 2)

        testState(bloc, Decrement, 1)

        testState(bloc, Whatever, 6)

        bloc.reduce { state - 4 }
        testState(bloc, null, 2)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun testReducerExecutionOrder() = runTest {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
            reduce { state + 5 }
        }

        assertEquals(1, bloc.value)
        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testCollectState(bloc, listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)) {
            repeat(10) {
                bloc.send(Increment)
                delay(10)
            }
        }

        testCollectState(bloc, listOf(11,10,9,8,7,6,5,4,3,2,1)) {
            repeat(10) {
                bloc.send(Decrement)
                delay(10)
            }
        }

        testCollectState(bloc, listOf(1,6,11,16,21,26,31,36,41,46,51)) {
            repeat(10) {
                bloc.send(Whatever)
                delay(10)
            }
        }

        testCollectState(bloc, listOf(51, 52, 57, 56)) {
            repeat(1) {
                bloc.send(Increment)
                delay(10)
                bloc.send(Whatever)
                delay(10)
                bloc.send(Decrement)
                delay(10)
            }
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun testReducerDuplicates() = runTest {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> { 7 }
            reduce<Decrement> { 6 }
            reduce { 5 }
        }

        assertEquals(1, bloc.value)
        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testCollectState(bloc, listOf(1, 7)) {
            repeat(10) {
                bloc.send(Increment)
                delay(10)
            }
        }

        testCollectState(bloc, listOf(7, 6, 5)) {
            repeat(10) {
                bloc.send(Increment)
                delay(10)
            }
            repeat(10) {
                bloc.send(Decrement)
                delay(10)
            }
            repeat(10) {
                bloc.send(Whatever)
                delay(10)
            }
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

}
