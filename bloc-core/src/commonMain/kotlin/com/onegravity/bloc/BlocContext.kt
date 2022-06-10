package com.onegravity.bloc

import com.arkivanov.essenty.lifecycle.LifecycleOwner

/**
 * Each Bloc receives a BlocContext.
 *
 * Right now, the context only contains a Lifecycle object which is managed by the caller and
 * determines when a Bloc starts, stops and is destroyed.
 *
 * Future versions of the BlocContext might have more properties / settings.
 */
public interface BlocContext : LifecycleOwner
