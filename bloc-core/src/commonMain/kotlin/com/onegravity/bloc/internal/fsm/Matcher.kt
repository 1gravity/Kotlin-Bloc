/** From https://github.com/Tinder/StateMachine */

package com.onegravity.bloc.internal.fsm

import kotlin.reflect.KClass

/**
 * @param clazz The class we're trying to match against with the matches(value: SuperClazz) function
 * @param value The value we're trying to match against with the equals() function. We need this to
 * determine whether two Matchers are equal when the Matcher is built using Enum values. When using
 * Enum values, KClass<ChildClazz> is identical for all the values (it's the enum class). In this
 * case we use the value parameter which is identical to the Enum value itself (not the Enum class).
 */
public class Matcher<SuperClazz : Any, out ChildClazz : SuperClazz> constructor(
    private val clazz: KClass<ChildClazz>,
    private val value: SuperClazz? = null
) {

    // this is supposed to be supported according to https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class:
    // fun clazzName() = clazz.qualifiedName
    // but it doesn't compile for JS so we use this instead
    internal fun clazzName() = clazz.simpleName + clazz.hashCode()

    private val defaultPredicate: (SuperClazz) -> Boolean = { superClazz -> clazz.isInstance(superClazz) }

    private val predicates = mutableListOf(defaultPredicate)

    public fun where(predicate: ChildClazz.() -> Boolean): Matcher<SuperClazz, ChildClazz> = apply {
        predicates.add { superClazz ->
            @Suppress("UNCHECKED_CAST")
            (superClazz as ChildClazz).predicate()
        }
    }

    internal fun matches(value: SuperClazz) = predicates.all { it(value) }

    public companion object {
        public inline fun <SuperClazz : Any, reified ChildClazz : SuperClazz> any(value: SuperClazz? = null):
                Matcher<SuperClazz, ChildClazz> = Matcher(ChildClazz::class, value)

        public inline fun <SuperClazz : Any, reified ChildClazz : SuperClazz> eq(value: ChildClazz):
                Matcher<SuperClazz, ChildClazz> = any<SuperClazz, ChildClazz>().where { this == value }

        /**
         * Use this for enum values
         */
        public inline fun <SuperClazz : Enum<SuperClazz>, reified ChildClazz : SuperClazz> eq(value: ChildClazz):
                Matcher<SuperClazz, ChildClazz> =
            any<SuperClazz, ChildClazz>(value).where { name == value.name }
    }

    public override fun equals(other: Any?): Boolean = when {
        other == null -> false
        other !is Matcher<*, *> -> false
        other.clazzName() == clazzName() && other.value == value -> true
        else -> false
    }

    public override fun hashCode(): Int = clazzName().hashCode()

}
