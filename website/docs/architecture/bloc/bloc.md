---
id: bloc
title: Bloc (Business Logic Component)
sidebar_label: Overview
hide_title: true
---

![Bloc Architecture - Details](../../../static/img/BLoC%20Architecture%20-%20BLoC%20Details.svg)


A **Bloc** implements the app's business logic. It processes event data (called `Action`) from the view / ui component and:
1. updates `State` according to its busines rules and emits the updated `State` to be consumed by the view / ui component.
2. optionally creates `SideEffect(s)` which can be consumed by the view / ui component (e.g. for navigation) or by other consumers (analytics events, logging etc.).

## Public Interface

A `Bloc` implements three public facing functions.

### State Stream

A stream to observe `State` called a `StateStream`. It's identical to [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) except it doesn't expose the `replayCache`.

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

A `StateStream` emits:
- all values, even duplicates
- no initial value upon subscription (analogous `PublishSubject`)

Note: because `StateStream` and `SideEffectStream` have a function with the same signature -> `collect(FlowCollector)`, the latter is accessible through the bloc's `sideEffects` property while the former is implemented by the bloc directly:

```kotlin
bloc.collect { /* observe State here */ }
bloc.sideEffects.collect { /* observe side effects here */ }
```

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

Above definition is the offical Redux reducer definition and captures its essence, although reducers in the context of `Kotlin BLoC` are a bit more complex: 
```kotlin
suspend (State, Action, CoroutineScope) -> Proposal
```
Compared to a Redux reducer, this one is:
1. suspending
2. takes a CoroutineScope as parameter (on top of the `State` and the `Action`)
3. returns a `Proposal` instead of `State`

More details can be found in [Reducer](./bloc/reducer).

Reducers are asynchronous in nature when they are defined using one of the [BlocBuilders](./bloc/bloc_builder). They can be synchronous when defined using [BlowOwner](./blocowner/bloc_owner) extensions.

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
suspend (State, Action, CoroutineScope) -> Proposal

// with side effects
suspend (State, Action, CoroutineScope) -> Effect<Proposal, SideEffect>
```

When the framework detects an `Effect` it will emit the side effects to a dedicated `Stream` that can be observed separately from the regular `State` stream. There's a DSL that make it easy to "build" reducers with side effects (see [Reducer](./bloc/reducer)).

## Thunk

While reducers are normally asynchronous in nature, their intented purpose is to update `State` right away to make sure the user interface is responsive to user input and updates "without" perceptible delay.
Longer running operations should be executed using a `Thunk`:
>The word "thunk" is a programming term that means "a piece of code that does some delayed work". Rather than execute some logic now, we can write a function body or code that can be used to perform the work later.  
https://redux.js.org/usage/writing-logic-thunks

A `Thunk` in the context of `Kotlin BLoC` is exactly what above definition implies, although its implementation and especially its execution is completely different from a Redux thunk. While the latter is a function, dispatched as an action to a Redux store and processed by the redux-thunk middleware, "our" thunk is not dispatched as an action but triggered the same way a reducer is triggered, by reacting to an `Action` that was sent to the `Bloc`. On top of that it's also:
1. a suspending function
2. takes a CoroutineScope as parameter (next to the `GetState`, `Action` and `Dispatcher` parameters)
3. Actions are dispatched to the "next" thunk or reducer in the execution chain (details see [Concurrency](./bloc/concurrency))

Details can be found in [Thunk](./bloc/thunk).
