package com.onegravity.bloc.internal

internal sealed class Action
internal object Increment : Action()
internal object Decrement : Action()
internal object Whatever : Action()

internal sealed class SideEffect
internal object Open : SideEffect()
internal object Close : SideEffect()
internal object Something : SideEffect()
