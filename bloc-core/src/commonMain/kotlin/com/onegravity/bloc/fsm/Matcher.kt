/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.fsm

import kotlin.reflect.KClass

class Matcher<SuperClazz : Any, out ChildClazz : SuperClazz> constructor(
    private val clazz: KClass<ChildClazz>
) {

    // this is supposed to be supported according to https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class:
    // fun clazzName() = clazz.qualifiedName
    // but it doesn't compile for JS so we use this instead
    fun clazzName() = clazz.simpleName + clazz.hashCode()

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

        inline fun <SuperClazz : Enum<SuperClazz>, reified ChildClazz : SuperClazz> eq(value: ChildClazz):
                Matcher<SuperClazz, ChildClazz> =
            any<SuperClazz, ChildClazz>().where { name == value.name }
    }

    override fun equals(other: Any?) = when {
        other == null -> false
        other !is Matcher<*, *> -> false
        other.clazzName() == clazzName() -> true
        else -> false
    }

    override fun hashCode() = clazzName().hashCode()

}
