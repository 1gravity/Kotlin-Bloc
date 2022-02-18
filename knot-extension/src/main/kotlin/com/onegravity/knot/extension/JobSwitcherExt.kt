package com.onegravity.knot.extension

import androidx.lifecycle.Lifecycle
import com.onegravity.knot.JobSwitcher

//import com.onegravity.reduce.
fun JobSwitcher.connectTo(lifecycle: Lifecycle) {
    lifecycle.addObserver(KnotLifecycleObserver(lifecycle, this))
}