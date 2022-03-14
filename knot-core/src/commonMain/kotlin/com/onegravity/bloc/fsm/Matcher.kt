package com.onegravity.bloc.fsm

import kotlin.reflect.KClass

class Matcher<T : Any, out R : T> private constructor(private val clazz: KClass<R>) {

    private val predicates = mutableListOf<(T) -> Boolean>({ clazz.isInstance(it) })

    fun where(predicate: R.() -> Boolean): Matcher<T, R> = apply {
        predicates.add {
            @Suppress("UNCHECKED_CAST")
            (it as R).predicate()
        }
    }

    fun matches(value: T) = predicates.all { it(value) }

    companion object {
        fun <T : Any, R : T> any(clazz: KClass<R>): Matcher<T, R> = Matcher(clazz)

        inline fun <T : Any, reified R : T> any(): Matcher<T, R> = any(R::class)

        inline fun <T : Any, reified R : T> eq(value: R): Matcher<T, R> = any<T, R>().where { this == value }
    }
}
