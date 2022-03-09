/**
 * Adapted version from: https://github.com/reduxkotlin/Reselect
 */

package com.onegravity.knot.select

/**
 * Wrapper class for Selector factory methods, that basically is used only to capture type
 * information for the state parameter
 */
class SelectorBuilder<S : Any> {
    /**
     * Special single input selector that should be used when you just want to retrieve a single field:
     * Warning: Don't use this with primitive type fields, use [withSingleFieldByValue] instead!!!
     */
    fun <I : Any> withSingleField(fn: S.() -> I) = object : AbstractSelector<S, I>() {
        @Suppress("UNCHECKED_CAST")
        private val inputField = InputField(fn, byRefEqualityCheck) as SelectorInput<Any, Any>

        override val computeAndCount = fun(i: Array<out Any>): I {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return i[0] as I
        }

        override operator fun invoke(state: S): I {
            return memoizer.memoize(state, inputField)
        }

        override val equalityCheck: EqualityCheckFn
            get() = byRefEqualityCheck

        override val memoizer: Memoizer<I> by lazy {
            singleInputMemoizer(computeAndCount)
        }
    }

    /**
     * Special single input selector that should be used when you just want to retrieve a single
     * field that is a primitive type like Int, Float, Double, etc..., because it compares memoized
     * values, instead of references
     */
    fun <I : Any> withSingleFieldByValue(fn: S.() -> I) = object : AbstractSelector<S, I>() {
        @Suppress("UNCHECKED_CAST")
        private val inputField = InputField(fn, byValEqualityCheck) as SelectorInput<Any, Any>
        override val computeAndCount = fun(i: Array<out Any>): I {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return i[0] as I
        }
        override operator fun invoke(state: S): I {
            return memoizer.memoize(state, inputField)
        }
        override val equalityCheck: EqualityCheckFn
            get() = byValEqualityCheck
        override val memoizer: Memoizer<I> by lazy {
            singleInputMemoizer(computeAndCount)
        }

        operator fun <I : Any> invoke(fn: S.() -> I): AbstractSelector<S, I> {
            return withSingleField(fn)
        }
    }
}
