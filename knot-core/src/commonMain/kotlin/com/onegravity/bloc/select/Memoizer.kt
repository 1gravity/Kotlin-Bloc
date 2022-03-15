/**
 * Adapted version from: https://github.com/reduxkotlin/Reselect
 */

package com.onegravity.bloc.select

typealias EqualityCheckFn = (a: Any, b: Any) -> Boolean

/**
 * A rewrite for kotlin of https://github.com/reactjs/reselect library for redux (https://github.com/reactjs/redux)
 * see also "Computing Derived Data" in redux documentation http://redux.js.org/docs/recipes/ComputingDerivedData.html
 * Created by Dario Elyasy  on 3/18/2016.
 */

/**
 * equality check by reference
 */
val byRefEqualityCheck: EqualityCheckFn = { a: Any, b: Any -> a === b }

/**
 * equality check by value: for primitive type
 */
val byValEqualityCheck: EqualityCheckFn = { a: Any, b: Any -> a == b }

interface Memoizer<T> {
    fun memoize(state: Any, vararg inputs: SelectorInput<Any, Any>): T
}

// {a:Any,b:Any -> a===b}
fun <T> computationMemoizer(computeFn: (Array<out Any>) -> T) = object : Memoizer<T> {
    var lastArgs: Array<out Any>? = null
    var lastResult: T? = null
    override fun memoize(state: Any, vararg inputs: SelectorInput<Any, Any>): T {
        val nInputs = inputs.size
        val args = Array<Any>(nInputs) { inputs[it].invoke(state) }
        if (lastArgs != null && lastArgs!!.size == inputs.size) {
            var bMatchedArgs = true
            for (i in 0 until nInputs) {
                if (!inputs[i].equalityCheck(args[i], lastArgs!![i])) {
                    bMatchedArgs = false
                    break
                }
            }
            if (bMatchedArgs) {
                return lastResult!!
            }
        }
        lastArgs = args
        lastResult = computeFn(args)
        return lastResult!!
    }
}

/**
 * specialization for the case of single input (a little bit faster)
 */
fun <T> singleInputMemoizer(func: (Array<out Any>) -> T) = object : Memoizer<T> {
    var lastArg: Any? = null
    var lastResult: T? = null
    override fun memoize(state: Any, vararg inputs: SelectorInput<Any, Any>): T {
        val input = inputs[0]
        val arg = input.invoke(state)
        if (lastArg != null &&
            input.equalityCheck(arg, lastArg!!)
        ) {
            return lastResult!!
        }
        lastArg = arg
        lastResult = func(arrayOf(arg))
        return lastResult!!
    }
}
