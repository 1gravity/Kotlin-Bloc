package com.onegravity.bloc.internal

/**
 * Actions can be complex objects resulting in too much of debug output
 */
internal fun Any.trimOutput() = toString().take(100)
