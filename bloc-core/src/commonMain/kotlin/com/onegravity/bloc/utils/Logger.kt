package com.onegravity.bloc.utils

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

