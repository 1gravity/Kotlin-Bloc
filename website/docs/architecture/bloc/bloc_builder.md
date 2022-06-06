---
id: bloc_builder
title: Bloc Builder
sidebar_label: Bloc Builder
hide_title: true
---

## Bloc DSL

There's a DSL to make the definition of `Blocs` easy. We have encountered some of that DSL already in the chapters about [Reducers](./reducer), [Thunks](./thunk) and [Initializers](./initializer). Here's a (dummy) example to give an overview of all of possible functions :

```kotlin
data class CountState(val count: Int)

sealed class CountAction
object Increment : CountAction()
object Decrement : CountAction()

sealed class SideEffect
object LogEvent : SideEffect()

fun bloc(context: BlocContext) = bloc<CountState, CountAction, SideEffect, CountState>(
    context,
    blocState(CountState(1))
) {
    // Initializer
    onCreate {
        logger.d("Bloc is starting")
    }

    // Thunk
    thunk {
        logger.d("Run an asynchronous operation")
        dispatch(action)
    }

    // Reducer with side effect
    reduceAnd<Increment> {
        state.copy(count = state.count + 1) and LogEvent
    }
    // Reducer without side effect
    reduceAnd<Decrement> {
        state.copy(count = state.count + 1).noSideEffect()
    }

    // Reducers without side effects
    reduce<Increment> { state.copy(count = state.count + 1) }
    reduce<Decrement> { state.copy(count = state.count - 1) }

    // side effect
    sideEffect<Decrement> { LogEvent }

    // catch-all reducer with side effect
    reduce {
        when (action) {
            is Increment -> state.copy(count = state.count + 1)
            else -> state.copy(count = state.count - 1)
        }
    }

    // catch-all reducer without side effect
    reduceAnd {
        when (action) {
            is Increment -> state.copy(count = state.count + 1).noSideEffect()
            else -> state.copy(count = state.count - 1) and LogEvent
        }
    }
}
```

## BlocBuilder

While this is great to define the `Bloc` functions, there are also helper functions that make the process of declaring `Blocs` even simpler/shorter. In above example the `Bloc` was declared using the full syntax:

```kotlin
bloc<CountState, CountAction, SideEffect, CountState>(
    context,
    blocState = blocState(CountState(1))
) {
```

If we only need a standard `BlocState` (see also [BlocStateBuilder](../blocstate/bloc_state_builder)) we can replace the `blocState` parameter by an `initialValue`: 

```kotlin
bloc<CountState, CountAction, SideEffect, CountState>(
    context,
    initialValue = CountState(1)
) {
```

The framework will create a `BlocState` with that initial value automatically.

In many cases the `State` and `Proposal` are identical (like in the example above) so we can get rid of the generic type for the `Proposal`:

```kotlin
// with blocState
bloc<CountState, CountAction, SideEffect>(
    context,
    blocState = blocState(CountState(1))
) {

// with initialValue
bloc<CountState, CountAction, SideEffect>(
    context,
    initialValue = CountState(1)
) {
```

Using this syntax the type of `Proposal` will be inferred as `State`.

If `SideEffects` aren't needed (more often than not we won't need them), we can simplify the syntax even more:

```kotlin
// with blocState
bloc<CountState, CountAction>(
    context,
    blocState = blocState(CountState(1))
) {

// with initialValue
bloc<CountState, CountAction>(
    context,
    initialValue = CountState(1)
) {
```

Using this syntax the type of `SideEffect` will be set to `Unit` (we can't use `Nothing` because side effects are of type `Any` and `Nothing` is not a sub type of `Any`).
