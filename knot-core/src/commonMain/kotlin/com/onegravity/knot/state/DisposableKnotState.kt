package com.onegravity.knot.state

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable

abstract class DisposableKnotState : Disposable {
    private val disposables = CompositeDisposable()

    fun Disposable.addDisposable() {
        disposables.add(this)
    }

    fun add(disposable: Disposable) {
        disposables.add(disposable)
    }
    override fun dispose() {
        disposables.dispose()
    }

    override val isDisposed = disposables.isDisposed
}
