package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BlocReducerExecutionTests : BaseTestClass() {

    @Test
    fun testReducerExecutionSimple() = runTests {
        val (bloc, lifecycleRegistry) = createBloc()

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        assertEquals(1, bloc.value)
        testState(bloc, Increment, 2)

        assertEquals(2, bloc.value)
        testState(bloc, Decrement, 1)

        assertEquals(1, bloc.value)
        testState(bloc, Whatever, 6)

        assertEquals(6, bloc.value)
        bloc.reduce { state - 4 }
        testState(bloc, null, 2)

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun testReducerExecutionNoDelays() = runTest {
        testReducerExecutionWithDelays(0, 0, 0, 0, 0, 0)
    }

    @Test
    fun testReducerExecutionSendDelays() = runTest {
        testReducerExecutionWithDelays(100, 100, 100, 0, 0, 0)
    }

    @Test
    fun testReducerExecutionReducerDelays() = runTest {
        testReducerExecutionWithDelays(0, 0, 0, 1000, 100, 0)
        testReducerExecutionWithDelays(0, 0, 0, 10, 100, 500)
    }

    @Test
    fun testReducerExecutionSendAndReducerDelays() = runTest {
        testReducerExecutionWithDelays(123, 1000, 500, 1000, 100, 500)
        testReducerExecutionWithDelays(10, 100, 0, 1000, 100, 500)
    }

    private fun testReducerExecutionWithDelays(
        delaySendInc: Long = 0,
        delaySendDec: Long = 0,
        delaySendWhatever: Long = 0,
        delayReducerInc: Long = 0,
        delayReducerDec: Long = 0,
        delayReducerWhatever: Long = 0
    ) = runTest {
        val (bloc, lifecycleRegistry) = createBloc(delayReducerInc, delayReducerDec, delayReducerWhatever)

        assertEquals(1, bloc.value)
        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        assertEquals(1, bloc.value)
        testCollectState(
            bloc,
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
            delayReducerInc.times(10).plus(100).coerceAtLeast(100)
        ) {
            repeat(10) {
                bloc.send(Increment)
                delay(delaySendInc)
            }
        }

        assertEquals(11, bloc.value)
        testCollectState(
            bloc,
            listOf(11,10,9,8,7,6,5,4,3,2,1),
            delayReducerDec.times(10).plus(100).coerceAtLeast(100)
        ) {
            repeat(10) {
                bloc.send(Decrement)
                delay(100)
                delay(delaySendDec)
            }
        }

        assertEquals(1, bloc.value)
        testCollectState(
            bloc,
            listOf(1,6,11,16,21,26,31,36,41,46,51),
            delayReducerWhatever.times(10).plus(100).coerceAtLeast(100)
        ) {
            repeat(10) {
                bloc.send(Whatever)
                delay(delaySendWhatever)
            }
        }

        assertEquals(51, bloc.value)
        testCollectState(
            bloc,
            listOf(51, 52,57,56, 57,62,61, 62,67,66),
            (delayReducerInc + delayReducerDec + delayReducerWhatever + 100).times(3).coerceAtLeast(100)
        ) {
            repeat(3) {
                bloc.send(Increment)
                delay(delaySendInc)
                bloc.send(Whatever)
                delay(delaySendWhatever)
                bloc.send(Decrement)
                delay(delaySendDec)
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

    private fun createBloc(
        delayInc: Long = 0,
        delayDec: Long = 0,
        delayWhatever: Long = 0
    ) : Pair<Bloc<Int, Action, Unit>, LifecycleRegistry> {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Action, Unit>(context, 1) {
            reduce<Increment> {
                delay(delayInc)
                state + 1
            }
            reduce<Decrement> {
                delay(delayDec)
                state - 1
            }
            reduce {
                delay(delayWhatever)
                state + 5
            }
        }
        return Pair(bloc, lifecycleRegistry)
    }

}
