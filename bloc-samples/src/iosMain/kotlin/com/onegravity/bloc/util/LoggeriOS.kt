package com.onegravity.bloc.util

import com.onegravity.bloc.utils.logger.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * If we want to use DI for the Logger on iOS, use this helper class
 */
class LoggeriOS : KoinComponent {
    private val logger : Logger by inject()
    fun logger() : Logger = logger
}
