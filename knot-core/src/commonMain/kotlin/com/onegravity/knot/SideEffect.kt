package com.onegravity.knot

data class SideEffect<out Event>(val block: suspend() -> Event?)
