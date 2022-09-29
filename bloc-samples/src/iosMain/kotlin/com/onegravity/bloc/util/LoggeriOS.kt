package com.onegravity.bloc.util

import com.onegravity.bloc.utils.logger.Logger

/**
 * If we want to use DI for the Logger on iOS, use this helper class
 */
object LoggeriOS {
    private val logger = getKoinInstance<Logger>()
    fun logger() = logger
}
