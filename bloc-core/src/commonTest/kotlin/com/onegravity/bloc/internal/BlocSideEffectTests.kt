package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.bloc
import com.onegravity.bloc.runTests
import com.onegravity.bloc.testCollectSideEffects
import kotlinx.coroutines.delay
import kotlin.test.Test

class BlocSideEffectTests : BaseTestClass() {

    @Test
    fun `test side effects - catch all`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, SideEffect>(context, 1) {
            sideEffect<Increment> { Open }
            sideEffect<Decrement> { Close }
            sideEffect { Something }
        }

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testCollectSideEffects(
            bloc,
            listOf(Something, Something, Something, Something, Something)
        ) {
            repeat(5) {
                bloc.send(Whatever)
                delay(10)
            }
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test side effects - action and catch all`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, SideEffect>(context, 1) {
            sideEffect<Increment> { Open }
            sideEffect<Decrement> { Close }
            sideEffect { Something }
        }

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testCollectSideEffects(bloc, listOf(Open, Something, Open, Something, Open, Something)) {
            repeat(3) {
                bloc.send(Increment)
                delay(10)
            }
        }
    }

    @Test
    fun `test side effects - multiple actions and catch all`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, SideEffect>(context, 1) {
            reduce<Increment> { state + 1 }
            sideEffect<Increment> { Open }
            sideEffect<Increment> { Open }
            sideEffect<Decrement> { Close }
            sideEffect { Something }
        }

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testCollectSideEffects(
            bloc,
            listOf(Open, Open, Something, Open, Open, Something, Open, Open, Something)
        ) {
            repeat(3) {
                bloc.send(Increment)
                delay(10)
            }
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test side effects - reducers and actions and catch all`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, SideEffect>(context, 1) {
            reduce<Increment> { state + 1 }
            sideEffect<Increment> { Open }
            sideEffect<Increment> { Open }
            sideEffect<Decrement> { Close }
            sideEffect { Something }
        }

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testCollectSideEffects(
            bloc,
            listOf(Open, Open, Something, Open, Open, Something, Open, Open, Something)
        ) {
            repeat(3) {
                bloc.send(Increment)
                delay(10)
            }
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test side effects - thunks and actions and catch all`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, SideEffect>(context, 1) {
            thunk<Increment> { dispatch(Decrement) }
            sideEffect<Increment> { Open }
            sideEffect<Increment> { Open }
            sideEffect<Decrement> { Close }
            sideEffect { Something }
        }

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testCollectSideEffects(
            bloc,
            listOf(Close, Something, Close, Something, Close, Something)
        ) {
            repeat(3) {
                bloc.send(Increment)
                delay(10)
            }
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

    @Test
    fun `test side effects - thunks and reducers and actions and catch all`() = runTests {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)

        val bloc = bloc<Int, Action, SideEffect>(context, 1) {
            thunk<Increment> { dispatch(Decrement) }
            reduce<Increment> { state + 1 }
            reduce<Decrement> { state - 1 }
            sideEffect<Increment> { Open }
            sideEffect<Increment> { Open }
            sideEffect<Decrement> { Close }
            sideEffect { Something }
        }

        lifecycleRegistry.onCreate()
        lifecycleRegistry.onStart()

        testCollectSideEffects(
            bloc,
            listOf(Close, Something, Close, Something, Close, Something)
        ) {
            repeat(3) {
                bloc.send(Increment)
                delay(10)
            }
        }

        lifecycleRegistry.onStop()
        lifecycleRegistry.onDestroy()
    }

}
