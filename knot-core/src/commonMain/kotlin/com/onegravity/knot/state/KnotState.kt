package com.onegravity.knot.state

import com.onegravity.knot.KnotFacade

/**
 * TODO we need a builder that allows to define the two functions accept and the mapping from State to Model
 * TODO we also want a KnotState implementation that connects to a Redux Store: accept, map, select functions
 * TODO we can also build this class using the tbd DSL for knotState:
 *   knotState<Proposal, State, Model> {
 *     accept {
 *     }
 *     map {
 *     }
 *     select { <-- optional, only with Redux
 *     }
 *   }
 */
interface KnotState<out State, in Action> : KnotFacade<State, Action>