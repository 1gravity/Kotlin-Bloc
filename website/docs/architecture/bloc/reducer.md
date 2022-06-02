---
id: reducer
title: Reducer
sidebar_label: Reducer
hide_title: true
---

![Bloc Architecture - Details](../../../static/img/BLoC%20Architecture%20-%20BLoC%20Details.svg)


## Definition

To reiterate:
> A reducer is a function that receives the current state and an action object, decides how to update the state if necessary, and returns the new state: (state, action) => newState  
(https://redux.js.org/tutorials/fundamentals/part-2-concepts-data-flow)

While the offical Redux reducer definition captures the essence, reducers in the context of `Kotlin BLoC` are a bit more complex: 
```kotlin
suspend (State, Action, CoroutineScope) -> Proposal
```
Compared to a Redux reducer, "our" reducer is:
1. suspending
2. takes a CoroutineScope as parameter (on top of the `State` and the `Action`)
3. returns a `Proposal` instead of `State`

## Scheduling

When an `Action` is sent to the Bloc, the Bloc will match the action to the thunks and reducers that are supposed to deal with that specific action. If there are matches, the action will be sent to the according queues (there's a queue for thunks and one for reducers). The queues are [Channels](https://kotlinlang.org/docs/channels.html) which are processed asynchronously. 

## Matcher

In the case of reducers, a match happens if:
1. the reducer doesn't define an action explicitly 
2. the reducer defines an action matching the sent action

### Without Action

A reducer without action is like a Redux reducer, a catch-all function to process a bunch of actions typically using a when statement like this:
```kotlin
reduce {
    when (action) {
        is Increment -> state + 1
        is Decrement -> state - 1
    }
}
```

### With Action

A reducer with action will be executed only if the sent action matches the reducer's action. There are two different types of actions requiring a different syntax:

If an action is a `class`/`object` or `data class`, the syntax is:
```kotlin
reduce<Increment> { state + 1 }
reduce<Decrement> { state - 1 }

// Action definition
sealed class Action
object Increment : Action()
object Decrement : Action()
```

If an action is an `enum class`, the syntax is:
```kotlin
reduce(Action.Increment) { state + 1 }
reduce(Action.Decrement) { state - 1 }

// Action definition
enum class Action { Increment, Decrement }
```

### A Matter of Taste

Actions can be "caught" and processed separately or they can be caught and processed in a single function/reducer (or we can use a combination of the two). It's a "matter of taste", which style you prefer. While the traditional/Redux style is to have reducers with a rather large scope, [this article](https://dev.to/feresr/a-case-against-the-mvi-architecture-pattern-1add) advocates for decomposition of reducers into smaller chunks. [Orbit](https://orbit-mvi.org/) is one of the frameworks that encourages decomposition of state reduction and served as blueprint for this part of the `Kotlin BLoC` framework.

## Concurrency

Reducers are asynchronous in nature when they are defined using one of the [BlocBuilders](./bloc/bloc_builder). They can be synchronous when defined using [BlowOwner](./blocowner/bloc_owner) extensions.

## Immutability


### Execution
### CoroutineScope

## Side Effect


https://github.com/genaku/Reduce