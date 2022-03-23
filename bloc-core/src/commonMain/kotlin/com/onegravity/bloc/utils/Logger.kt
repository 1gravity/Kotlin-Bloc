package com.onegravity.bloc.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger as KermitLogger

expect fun logger(): LogWriter

/**
 * A basic Logger interface.
 *
 * We use this internally to encapsulate the actual Logger which is Kermit atm.
 */
interface Logger {

    /**
     * Log debug messages
     */
    fun d(msg: String)

    /**
     * Log info messages
     */
    fun i(msg: String)

    /**
     * Log warning messages
     */
    fun w(msg: String)

    /**
     * Log error messages
     */
    fun e(msg: String, t: Throwable? = null)

}

object LoggerImpl : Logger {

    override fun d(msg: String) {
        KermitLogger.d(msg)
    }

    override fun i(msg: String) {
        KermitLogger.d(msg)
    }

    override fun w(msg: String) {
        KermitLogger.w(msg)
    }

    override fun e(msg: String, t: Throwable?) {
        when (t) {
            null -> KermitLogger.e(msg)
            else -> KermitLogger.e(msg, t)
        }

    }

}
