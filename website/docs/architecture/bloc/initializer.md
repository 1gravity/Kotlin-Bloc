---
id: initializer
title: Initializer
sidebar_label: Initializer
hide_title: true
---

## Definition

Initializers are functions executed when the bloc is created, typically to kick off some initial load. They can execute asynchronous code and dispatch actions to be processed by other thunks and reducers. Initializers are executed exactly once during the [Lifecycle](./lifecycle) of a bloc.

### Context

An initializer is called with a `InitializerContext` as receiver. The context is giving access to the current `State` and a `Dispatcher`:


```kotlin
public data class InitializerContext<State, Action>(
    val state: State,
    val dispatch: Dispatcher<Action>
)
```

Here's a typical example:

```kotlin
onCreate { 
    if (state.isEmpty()) dispatch(Load) 
}

thunk<Load> {
    dispatch(Loading)
    val result = repository.getPosts()
    dispatch(Loaded(result))
}
```

The thunk's asynchronous code could also be in the initializer itself:

```kotlin
onCreate { 
    if (state.isEmpty()) {
        dispatch(Loading)
        val result = repository.getPosts()
        dispatch(Loaded(result))
    }
}
```

The order of declaration is irrelevant, the initializer will always be called first. The bloc also waits for the initializer to finish before processing thunks or reducers. Actions dispatched before the initializer finishes (between `onCreate()` and `onStart()`) are sent to a queue and are processed once the bloc transitions to the `Started` state (see [Lifecycle](lifecycle)).
That's true for actions dispatched by the initializer itself and also for actions dispatched directly to the bloc (MVVM+ style). 
The only exception to that rule is if the initializer launches asynchronous code e.g. via [launch](coroutine_launcher) and would dispatch actions from there. 

:::tip
If more than one initializer is defined, the first one (according to their order of declaration) is used, all others are ignored.
:::
