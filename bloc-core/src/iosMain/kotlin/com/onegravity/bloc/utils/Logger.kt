package com.onegravity.bloc.utils

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.platformLogWriter

internal actual fun logWriter(): LogWriter = platformLogWriter()
