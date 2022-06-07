package com.onegravity.bloc.context

import com.arkivanov.essenty.lifecycle.Lifecycle

public class BlocContextImpl(
    override val lifecycle: Lifecycle
) : BlocContext
