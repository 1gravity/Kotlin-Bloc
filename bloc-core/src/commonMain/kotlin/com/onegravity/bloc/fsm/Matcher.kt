/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.fsm

import kotlin.reflect.KClass

/**
 * @param clazz The class we're trying to match against with the matches(value: SuperClazz) function
 * @param value The value we're trying to match against with the equals() function. We need this to
 * determine whether two Matchers are equal when the Matcher is built using Enum values. When using
 * Enum values, KClass<ChildClazz> is identical for all the values (it's the enum class). In this
 * case we use the value parameter which is identical to the Enum value itself (not the Enum class).
 */
class Matcher<SuperClazz : Any, out ChildClazz : SuperClazz> constructor(
    private val clazz: KClass<ChildClazz>,
    private val value: SuperClazz? = null
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
        inline fun <SuperClazz : Any, reified ChildClazz : SuperClazz> any(value: SuperClazz? = null):
                Matcher<SuperClazz, ChildClazz> = Matcher(ChildClazz::class, value)

        inline fun <SuperClazz : Any, reified ChildClazz : SuperClazz> eq(value: ChildClazz):
                Matcher<SuperClazz, ChildClazz> =
            any<SuperClazz, ChildClazz>().where { this == value }

        /**
         * Use this for enum values
         */
        inline fun <SuperClazz : Enum<SuperClazz>, reified ChildClazz : SuperClazz> eq(value: ChildClazz):
                Matcher<SuperClazz, ChildClazz> =
            any<SuperClazz, ChildClazz>(value).where { name == value.name }
    }

    override fun equals(other: Any?) = when {
        other == null -> false
        other !is Matcher<*, *> -> false
        other.clazzName() == clazzName() && other.value == value -> true
        else -> false
    }

    override fun hashCode() = clazzName().hashCode()

}
