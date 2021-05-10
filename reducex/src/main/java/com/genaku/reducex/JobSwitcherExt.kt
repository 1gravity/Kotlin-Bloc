package com.genaku.reducex

import androidx.lifecycle.Lifecycle
import com.genaku.reduce.JobSwitcher

fun JobSwitcher.connectTo(lifecycle: Lifecycle) {
    lifecycle.addObserver(KnotLifecycleObserver(lifecycle, this))
}