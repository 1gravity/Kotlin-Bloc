package com.onegravity.bloc.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

// called by Android and iOS
fun initKoin(koinAppDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        modules(commonModule, dbModule)
        koinAppDeclaration()
    }
}

/**
 * Try not to use this function but use constructor injection instead.
 * Unfortunately we sometimes need it to accommodate for iOS.
 */
@Suppress("unused")
inline fun <reified T> getKoinInstance() =
    object : KoinComponent {
        val value: T by inject()
    }.value

/**
 * Try not to use this function but use constructor injection instead
 * Unfortunately we sometimes need it to accommodate for iOS.
 */
inline fun <reified S, reified T> getKoinInstances() =
    object : KoinComponent {
        val value1: S by inject()
        val value2: T by inject()
    }.run { value1 to value2 }
