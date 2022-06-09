package com.onegravity.bloc.internal

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.onegravity.bloc.BlocContext

public class BlocContextImpl(
    override val lifecycle: Lifecycle
) : BlocContext
