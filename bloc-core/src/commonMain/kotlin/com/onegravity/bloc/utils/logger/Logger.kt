package com.onegravity.bloc.utils.logger

/**
 * A basic Logger interface.
 *
 * We use this internally to encapsulate the actual Logger which is Kermit atm.
 */
public interface Logger {

    /**
     * Log debug messages
     */
    public fun d(msg: String)

    /**
     * Log info messages
     */
    public fun i(msg: String)

    /**
     * Log warning messages
     */
    public fun w(msg: String)

    /**
     * Log error messages
     */
    public fun e(msg: String, t: Throwable? = null)

}
