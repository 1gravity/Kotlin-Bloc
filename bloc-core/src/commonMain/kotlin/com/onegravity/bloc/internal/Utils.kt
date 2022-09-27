package com.onegravity.bloc.internal

private const val MAX_ACTION_LENGTH = 100

/**
 * Actions can be complex objects resulting in too much of debug output
 */
internal fun Any.trimOutput() = toString().take(MAX_ACTION_LENGTH)
