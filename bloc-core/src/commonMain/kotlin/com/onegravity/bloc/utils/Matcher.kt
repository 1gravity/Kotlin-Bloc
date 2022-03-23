/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.utils

import kotlin.reflect.KClass

class Matcher<SuperClazz : Any, out ChildClazz : SuperClazz> constructor(
    private val clazz: KClass<ChildClazz>
) {

    fun clazzName() = clazz.qualifiedName

    private val defaultPredicate: (SuperClazz) -> Boolean = { superClazz -> clazz.isInstance(superClazz) }

    private val predicates = mutableListOf(defaultPredicate)

    fun where(predicate: ChildClazz.() -> Boolean): Matcher<SuperClazz, ChildClazz> = apply {
        predicates.add { superClazz ->
            @Suppress("UNCHECKED_CAST")
            (superClazz as ChildClazz).predicate()
        }
    }

    fun matches(value: SuperClazz) = predicates.all { it(value) }

    companion object {
        inline fun <SuperClazz : Any, reified ChildClazz : SuperClazz> any():
                Matcher<SuperClazz, ChildClazz> = Matcher(ChildClazz::class)

        inline fun <SuperClazz : Any, reified ChildClazz : SuperClazz> eq(value: ChildClazz):
                Matcher<SuperClazz, ChildClazz> =
            any<SuperClazz, ChildClazz>().where { this == value }
    }

    override fun equals(other: Any?) = when {
        other == null -> false
        other !is Matcher<*, *> -> false
        other.clazzName() == clazzName() -> true
        else -> false
    }

    override fun hashCode() = clazzName().hashCode()

}
