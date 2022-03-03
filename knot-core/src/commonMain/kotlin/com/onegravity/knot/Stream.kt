package com.onegravity.knot

import kotlinx.coroutines.flow.FlowCollector

interface Stream<out Value> {
    val value: Value

    suspend fun collect(collector: FlowCollector<Value>): Nothing
}
