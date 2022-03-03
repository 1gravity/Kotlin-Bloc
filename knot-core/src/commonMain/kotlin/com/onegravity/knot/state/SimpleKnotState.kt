package com.onegravity.knot.state

import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.onegravity.knot.StreamReceiver

/**
 * This KnotState serves as value holder for State.
 * It accepts new values and serves them as value and stream.
 */
class SimpleKnotState<State>(initialState: State) :
    KnotState<State, State>,
    DisposableKnotState() {

    private val stream = BehaviorSubject(initialState)

    override fun send(value: State) {
        stream.onNext(value)
    }

    override fun receive(receiver: StreamReceiver<State>) {
        stream
            .subscribe { state -> receiver.emit(state) }
            .addDisposable()
    }

    override val value: State
        get() = stream.value

}
