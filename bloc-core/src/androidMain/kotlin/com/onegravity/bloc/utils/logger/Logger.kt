package com.onegravity.bloc.utils.logger

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.platformLogWriter

actual fun logger(): LogWriter = platformLogWriter()
