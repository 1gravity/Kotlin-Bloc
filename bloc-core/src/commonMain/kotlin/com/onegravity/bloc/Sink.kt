package com.onegravity.bloc

/**
 * A Sink is a generic destination for data.
 *
 * Unlike Flutter sinks, this one cannot be closed. There are also no errors (compared to e.g.
 * Flutter EventSink) because we're following a functional approach and use monads to model
 * success/failures (using https://github.com/michaelbull/kotlin-result).
 */
public interface Sink<in Value : Any> {

    /**
     * Send a Value into the sink.
     *
     * This is a synchronous operation but it depends on the receiver if the emitted value triggers
     * a synchronous or an asynchronous operation (or none at all).
     */
    public fun send(value: Value)

}
