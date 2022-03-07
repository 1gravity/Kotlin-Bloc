package com.onegravity.knot

/**
 * The KnotFacade is the "public" part of a Knot (and a KnotState).
 * It defines a Stream of 'State' and a Sink of 'Event' as the part of a Knot/KnotState that is
 * exposed to the UI and consuming Knots.
 */
interface KnotFacade<out State, in Event> : Stream<State>, Sink<Event>
