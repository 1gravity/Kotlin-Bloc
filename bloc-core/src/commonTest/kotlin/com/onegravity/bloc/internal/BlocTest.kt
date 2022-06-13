package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.bloc
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BlocTest {

    @Test
    fun firstTest() {
        val lifecycleRegistry = LifecycleRegistry()
        val context = BlocContextImpl(lifecycleRegistry)
        val bloc = bloc<Int, Int>(context, 1) {

        }
        assertEquals(1, bloc.value)
    }

}