---
id: bloc
title: Bloc (Business Logic Component)
sidebar_label: Overview
hide_title: true
---

## Overview

![Bloc Architecture - Details](../../../static/img/Bloc%20Architecture%20-%20Bloc%20Details.svg)


A **Bloc** implements the app's business logic. It processes event data (called `Action`) from the view / ui component and:
1. updates `State` according to its business rules and emits the updated `State` to be consumed by the view / ui component.
2. optionally creates `SideEffect(s)` which can be consumed by the view / ui component (e.g. for navigation) or by other consumers (analytics events, logging etc.).

## Public Interface

A `Bloc` implements three public facing functions.

### State Stream

A stream to observe `State` called a `StateStream`. It's similar to [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) but doesn't expose the `replayCache` and doesn't [conflate values](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/conflate.html).

```kotlin
public val value: State
public suspend fun collect(collector: FlowCollector<State>)
```

While `value` is used to retrieve the current `State`, the `collect` function is used to collect the flow of `States` emitted by the `Bloc`. 

A `StateStream` emits:
- no duplicate values
- an initial value upon subscription (analogous `BehaviorSubject`)

### SideEffect Stream

A stream to observe `SideEffects` called a `SideEffectStream`. It's identical to [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/).


```kotlin
public suspend fun collect(collector: FlowCollector<SideEffect>)
```

A `SideEffectStream` emits:
- all values, even duplicates
- no initial value upon subscription (analogous `PublishSubject`)

:::tip
Because `StateStream` and `SideEffectStream` have a function with the same signature -> `collect(FlowCollector)`, the latter is accessible through the bloc's `sideEffects` property while the former is implemented by the bloc directly:
```kotlin
bloc.collect { /* observe State here */ }
bloc.sideEffects.collect { /* observe side effects here */ }
```
:::

There's no need to call above functions directly. There are extension functions / wrappers that make observing blocs very easy -> [Extensions](../extensions/overview).

### Sink

A sink to send data / `Actions` to the `Bloc`:

```kotlin
public fun send(action: Action)
```

While this is not a suspending function, it immediately returns after adding the action to a queue / channel.

## Reducer

Processing an `Action` usually means invoking a `Reducer`:
> A reducer is a function that receives the current state and an action object, decides how to update the state if necessary, and returns the new state: (state, action) => newState  
(https://redux.js.org/tutorials/fundamentals/part-2-concepts-data-flow)

Above definition is the official Redux reducer definition and captures its essence. Reducers in the context of `Kotlin Bloc` are very similar: 

```kotlin
(State, Action) -> Proposal
```

Compared to a Redux reducer, this one returns a `Proposal` instead of `State`. More details can be found in [Reducer](./bloc/reducer).

## Side Effect

Side effects are created by "special" reducers that return an `Effect` object. 

`Effects` are simple data classes encapsulating the new proposed state and a list of side effects:

```kotlin
public data class Effect<Proposal : Any, SideEffect : Any>(
    val proposal: Proposal?,
    val sideEffects: List<SideEffect>
)
```

The difference between a "regular" reducer and one with side effects is simply the nature of its `Proposal`:

```kotlin
// no side effects
(State, Action) -> Proposal

// with side effects
(State, Action) -> Effect<Proposal, SideEffect>
```

When the framework detects an `Effect` it will emit the side effects to a dedicated `Stream` that can be observed separately from the regular `State` stream. There's a DSL that make it easy to "build" reducers with side effects (see [Reducer](./bloc/reducer)).

## Thunk

Reducers are synchronous in nature and their intended purpose is to update `State` right away to make sure the user interface is responsive to user input and updates "without" perceptible delay. Longer running operations should be executed using a `Thunk`:
>The word "thunk" is a programming term that means "a piece of code that does some delayed work". Rather than execute some logic now, we can write a function body or code that can be used to perform the work later.  
https://redux.js.org/usage/writing-logic-thunks

A `Thunk` in the context of `Kotlin Bloc` is exactly what above definition implies, although its implementation and especially its execution is completely different from a Redux thunk. While the latter is a function, dispatched as an action to a Redux store and processed by the redux-thunk middleware, "our" thunk is not dispatched as an action but triggered the same way a reducer is triggered, by reacting to an `Action` that was sent to the `Bloc`. On top of that it's also:
1. a suspending function
2. actions are dispatched to the "next" thunk or reducer in the execution chain

Details can be found in [Thunk](./bloc/thunk).

## Initializer

Initializers are functions executed when the bloc is created. They are similar to thunks since they can execute asynchronous code and dispatch actions to be processed by other thunks and reducers. Unlike thunks, initializers are executed once and once only during the [Lifecycle](./bloc/lifecycle) of a bloc.

Details can be found in [Initializer](./bloc/initializer).
