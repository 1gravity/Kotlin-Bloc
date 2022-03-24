package com.onegravity.bloc.utils

import co.touchlab.kermit.Logger as KermitLogger

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
