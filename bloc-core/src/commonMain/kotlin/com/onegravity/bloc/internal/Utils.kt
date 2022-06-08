package com.onegravity.bloc.internal

// actions can be complex objects resulting in lots of debug output
internal fun Any.trimOutput() = toString().take(100)
