package com.genaku.reduce

/**
 * If your [Knot] becomes big and you want to improve its readability and maintainability,
 * you may consider to decompose it. You start decomposition by grouping related
 * concerns into, in a certain sense, indecomposable pieces called `Delegate`.
 *
 * [Flowchart diagram](https://github.com/beworker/knot/raw/master/docs/diagrams/flowchart-composite-knot.png)
 *
 * Each `Delegate` is isolated from the other `Delegates`. It defines its own set of
 * `Changes`, `Actions` and `Reducers`. It's only the `State`, that is shared between
 * the `Delegates`. In that respect each `Delegate` can be seen as a separate [Knot] instance.
 *
 * Once all `Delegates` are registered at a `CompositeKnot`, the knot can be finally
 * composed using [compose] function and start operating.
 *
 * See [Composite ViewModel](https://www.halfbit.de/posts/composite-viewmodel/) for more details.
 */
interface CompositeKnot<State : Any> : Knot<State, Any> {

    /** Registers a new `Delegate` in this composite knot. */
    fun <Change : Any, Action : Any> registerDelegate(
        block: DelegateBuilder<State, Change, Action>.() -> Unit
    )

    /** Finishes composition of `Delegates` and moves this knot into the operational mode. */
    fun compose()
}

interface DelegateBuilder<State, Change, Action> {

}
