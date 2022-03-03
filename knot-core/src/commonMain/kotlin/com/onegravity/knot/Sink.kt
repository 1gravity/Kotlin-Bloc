package com.onegravity.knot

interface Sink<in Value> {
    fun emit(value: Value)
}
